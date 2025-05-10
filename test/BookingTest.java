import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.Srent_DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BookingTest {
    private Srent_DB srentDB;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        srentDB = new Srent_DB();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        Mockito.mockStatic(Srent_DB.class);
        when(Srent_DB.getConnection()).thenReturn(mockConnection);
    }

    @Test
    void reserveCarSucceedsWithValidUserAndCar() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean result = Srent_DB.reserveCar(1, 101);

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).setInt(2, 101);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    void reserveCarFailsWithInvalidUserOrCar() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean result = Srent_DB.reserveCar(999, 999);

        assertFalse(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 999);
        verify(mockPreparedStatement, times(1)).setInt(2, 999);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    void cancelReservationSucceedsWithValidBookingId() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean result = Srent_DB.cancelReservation(123);

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 123);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    void cancelReservationFailsWithInvalidBookingId() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean result = Srent_DB.cancelReservation(999);

        assertFalse(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 999);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }
}