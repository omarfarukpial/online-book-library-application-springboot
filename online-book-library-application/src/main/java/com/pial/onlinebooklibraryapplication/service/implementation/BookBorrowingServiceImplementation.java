package com.pial.onlinebooklibraryapplication.service.implementation;


import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingInfoDto;
import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.dto.UserDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReserveEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.exception.BookIdNotFoundException;
import com.pial.onlinebooklibraryapplication.exception.BookNotBorrowedException;
import com.pial.onlinebooklibraryapplication.exception.NotAuthorizedException;
import com.pial.onlinebooklibraryapplication.exception.UserIdNotFoundException;
import com.pial.onlinebooklibraryapplication.repository.BookBorrowRepository;
import com.pial.onlinebooklibraryapplication.repository.BookRepository;
import com.pial.onlinebooklibraryapplication.repository.BookReserveRepository;
import com.pial.onlinebooklibraryapplication.repository.UserRepository;
import com.pial.onlinebooklibraryapplication.service.BookBorrowingService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookBorrowingServiceImplementation implements BookBorrowingService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookBorrowRepository bookBorrowRepository;

    @Autowired
    private BookReserveRepository bookReserveRepository;

    public BookBorrowingDto bookBorrowing(Long bookId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        if (bookEntity == null || bookEntity.isDeleted()) throw new BookIdNotFoundException("Book does not exits!");
        if (Objects.equals(bookEntity.getStatus(), "BORROWED")) throw new Exception("Currently unavailable, but you can reserve this book!");

        ModelMapper modelMapper = new ModelMapper();
        BookBorrowingEntity bookBorrowingEntity = new BookBorrowingEntity();
        bookBorrowingEntity.setBookEntity(bookEntity);
        bookBorrowingEntity.setUserEntity(userEntity);
        bookBorrowingEntity.setBorrowDate(LocalDate.now());
        bookBorrowingEntity.setDueDate(LocalDate.now().plusDays(14));
        bookEntity.setStatus("BORROWED");


        BookBorrowingEntity storeBorrowDetails = bookBorrowRepository.save(bookBorrowingEntity);

        return modelMapper.map(storeBorrowDetails, BookBorrowingDto.class);
    }


    public BookBorrowingDto bookReturning(Long bookId) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        if (bookEntity == null || bookEntity.isDeleted()) throw new BookIdNotFoundException("Book does not exits!");

        BookBorrowingEntity bookBorrowingEntity = bookBorrowRepository.findByUserEntityAndBookEntityAndReturnDateIsNull(userEntity,bookEntity);
        if (bookBorrowingEntity == null) throw new BookNotBorrowedException("You did not currently borrowing this book!");
        ModelMapper modelMapper = new ModelMapper();
        bookBorrowingEntity.setReturnDate(LocalDate.now());
        bookEntity.setStatus("AVAILABLE");

        List<BookReserveEntity> bookReserveEntity = bookReserveRepository.findAllByBookEntityAndStatus(bookEntity,"PENDING");
        if(!bookReserveEntity.isEmpty()) {
            for (BookReserveEntity reserveEntity : bookReserveEntity) {
                reserveEntity.setStatus("DONE");
            }
        }

        BookBorrowingEntity storeReturnDetails = bookBorrowRepository.save(bookBorrowingEntity);
        return modelMapper.map(storeReturnDetails, BookBorrowingDto.class);
    }

    public List<BookEntity> getAllBookByUser(Long userId) throws NotAuthorizedException, UserIdNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();
        if (!currentUserId.equals(userId) && currentUserRole.equals("CUSTOMER")) {
            throw new NotAuthorizedException("You are not authorized to access this!");
        }
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UserIdNotFoundException("User Id does not exists!");
        List<BookBorrowingEntity> bookBorrowings = bookBorrowRepository.findAllByUserEntity(userEntity);

        List<BookEntity> books = bookBorrowings.stream()
                .map(BookBorrowingEntity::getBookEntity)
                .collect(Collectors.toList());

        return books;

    }

    public List<BookEntity> getAllBorrowedBookByUser(Long userId) throws NotAuthorizedException,UserIdNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();
        if (!currentUserId.equals(userId) && currentUserRole.equals("CUSTOMER")) {
            throw new NotAuthorizedException("You are not authorized to access this!");
        }
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UserIdNotFoundException("User Id does not exists!");

        List<BookBorrowingEntity> bookBorrowings = bookBorrowRepository.findAllByUserEntityAndReturnDateIsNull(userEntity);

        List<BookEntity> books = bookBorrowings.stream()
                .map(BookBorrowingEntity::getBookEntity)
                .collect(Collectors.toList());

        return books;

    }

    public List<BookBorrowingInfoDto> getUserAllHistory(Long userId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();
        UserEntity checkUser = userRepository.findByUserId(userId);
        if (checkUser == null) throw new UserIdNotFoundException("User id does not exists!");
        if (!currentUserId.equals(userId) && currentUserRole.equals("CUSTOMER")) {
            throw new NotAuthorizedException("You are not authorized to access this!");
        }
        UserEntity userEntity = userRepository.findByUserId(userId);
        List<BookBorrowingEntity> bookBorrowings = bookBorrowRepository.findAllByUserEntity(userEntity);
        if (bookBorrowings.isEmpty()) throw new BookNotBorrowedException("No book borrowed by this user!");
        List<BookBorrowingInfoDto> bookBorrowingInfoList = bookBorrowings.stream()
                .map(bookBorrowingEntity -> BookBorrowingInfoDto.builder()
                        .borrowId(bookBorrowingEntity.getBorrowId())
                        .bookEntity(bookBorrowingEntity.getBookEntity())
                        .borrowDate(bookBorrowingEntity.getBorrowDate())
                        .dueDate(bookBorrowingEntity.getDueDate())
                        .returnDate(bookBorrowingEntity.getReturnDate())
                        .build()
                )
                .collect(Collectors.toList());
        return bookBorrowingInfoList;
    }






}
