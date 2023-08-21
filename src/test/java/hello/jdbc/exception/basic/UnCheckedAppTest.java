package hello.jdbc.exception.basic;

import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnCheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request()).isInstanceOf(RuntimeSqlException.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }


    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException();
        }
    }

    static class Repository {
        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSqlException(e);
            }
        }
    }

    static class RuntimeSqlException extends RuntimeException{
        public RuntimeSqlException(Throwable cause) {
            super(new SQLException("ex"));
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException() {
        }
    }


}
