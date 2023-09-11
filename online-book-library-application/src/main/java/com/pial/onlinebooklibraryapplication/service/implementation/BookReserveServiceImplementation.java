package com.pial.onlinebooklibraryapplication.service.implementation;

import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookReserveDto;
import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReserveEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.repository.BookRepository;
import com.pial.onlinebooklibraryapplication.repository.BookReserveRepository;
import com.pial.onlinebooklibraryapplication.repository.UserRepository;
import com.pial.onlinebooklibraryapplication.service.BookReserveService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class BookReserveServiceImplementation implements BookReserveService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookReserveRepository bookReserveRepository;

    public BookReserveDto reserveBook(Long bookId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        if (Objects.equals(bookEntity.getStatus(), "AVAILABLE")) throw new Exception("This book is already available, you can borrow this!");


        ModelMapper modelMapper = new ModelMapper();
        BookReserveEntity bookReserveEntity = new BookReserveEntity();
        bookReserveEntity.setBookEntity(bookEntity);
        bookReserveEntity.setUserEntity(userEntity);
        bookReserveEntity.setReserveDate(LocalDate.now());
        bookReserveEntity.setStatus("PENDING");


        BookReserveEntity storeReserve = bookReserveRepository.save(bookReserveEntity);
        return modelMapper.map(storeReserve, BookReserveDto.class);

    }

    public BookReserveDto cancelReserveBook(Long bookId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        ModelMapper modelMapper = new ModelMapper();
        BookReserveEntity bookCancelReserveEntity = bookReserveRepository.findByBookEntity(bookEntity);

        if (bookCancelReserveEntity.getUserEntity() != userEntity) throw new Exception("You are not authorized to cancel reservation!");


        bookCancelReserveEntity.setStatus("CANCEL");

        BookReserveEntity cancelReserve = bookReserveRepository.save(bookCancelReserveEntity);
        return modelMapper.map(cancelReserve, BookReserveDto.class);

    }
}
