package be.kata.service;

import be.kata.api.model.CartItem;
import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CartServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CartService cartService = new CartService(userRepository, bookRepository);

    @Test
    void givenUserAndBookCount_whenSubmit_thenReturnTrue() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("User2");
        userEntity.setNrn("12345678902");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("B1");
        bookEntity.setName("Book1");
        bookEntity.setAuthor("Author1");
        bookEntity.setCount(2);
        bookEntity.setPrice(5);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setId("B2");
        bookEntity2.setName("Book2");
        bookEntity2.setAuthor("Author2");
        bookEntity2.setCount(1);
        bookEntity2.setPrice(5);

        CartItem bookCount = new CartItem("B1", 1);
        CartItem bookCount2 = new CartItem("B2", 1);

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        when(bookRepository.findById("B1")).thenReturn(Optional.of(bookEntity));
        when(bookRepository.findById("B2")).thenReturn(Optional.of(bookEntity2));

        assertThat(cartService.submit(1, List.of(bookCount, bookCount2))).isTrue();

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(userEntity);
    }

    @Test
    void givenUserAndBookCountIsMoreThanStock_whenSubmit_thenException() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("User2");
        userEntity.setNrn("12345678902");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("B1");
        bookEntity.setName("Book1");
        bookEntity.setAuthor("Author1");
        bookEntity.setCount(2);
        bookEntity.setPrice(5);

        CartItem bookCount = new CartItem("B1", 3);

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(bookRepository.findById("B1")).thenReturn(Optional.of(bookEntity));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> cartService.submit(1, List.of(bookCount)));
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenUserAndBookCountAndNoBookInDB_whenSubmit_thenException() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("User2");
        userEntity.setNrn("12345678902");

        CartItem bookCount = new CartItem("B1", 1);

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(bookRepository.findById("B1")).thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> cartService.submit(1, List.of(bookCount)));
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenUserAndBookCountWithZero_whenSubmit_thenException() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("User2");
        userEntity.setNrn("12345678902");

        CartItem bookCount = new CartItem("B1", 0);
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> cartService.submit(1, List.of(bookCount)));
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenWrongUser_whenSubmit_thenException() {
        CartItem bookCount = new CartItem("B1", 0);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> cartService.submit(1, List.of(bookCount)));
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }
}
