package racingcar.domain;

import static java.nio.file.Files.move;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import racingcar.domain.extension.Moveable;

@DisplayName("RacingGame 클래스")
class RacingGameTest {

    private final Moveable trueMover = () -> true;
    private final Moveable falseMover = () -> false;

    @Nested
    @DisplayName("new 생성자는")
    class ContextNewConstructor {

        @Nested
        @DisplayName("자동차 클래스들과 라운드 정보가 주어진다면,")
        class DescribeCarsInput {

            private final Car[] cars = {
                new Car("A", trueMover),
                new Car("B", falseMover),
                new Car("C", trueMover),
            };

            @Test
            @DisplayName("생성을 성공 한다.")
            void ItCreated() {
                assertThatNoException().isThrownBy(() -> new RacingGame(cars));
            }

        }

        @Nested
        @DisplayName("Cars에 중복된 이름의 Car가 있다면,")
        class DescribeDuplicateCarsNameInput {

            private final Car[] duplicateNameCars = {
                new Car("A", trueMover),
                new Car("A", falseMover),
                new Car("A", trueMover),
            };

            @Test
            @DisplayName("IllegalArgumentException을 던진다.")
            void ItThrowIllegalArgumentException() {
                assertThatThrownBy(() -> new RacingGame(duplicateNameCars)).isInstanceOf(
                    IllegalArgumentException.class);
            }

        }

    }

    @Nested
    @DisplayName("play 메소드는")
    class ContextPlayMethod {

        private final Car[] cars = {
            new Car("A", falseMover),
            new Car("B", falseMover),
            new Car("C", trueMover),
        };

        @Nested
        @DisplayName("한번 호출되면,")
        class DescribeRacingGameRound1 {

            private final RacingGame racingGame = new RacingGame(cars);
            private final Car[] expectedCars = move();

            @Test
            @DisplayName("한판을 진행하고, 그 결과를 car에 반영한다.")
            void ItReturn1RoundResult() {
                racingGame.play();
                assertEqualAllCars(cars, expectedCars);
            }

            private Car[] move() {
                Car[] copy = {
                    new Car("A", falseMover),
                    new Car("B", falseMover),
                    new Car("C", trueMover),
                };
                for (Car car : copy) {
                    car.move();
                }
                return copy;
            }

        }

        @Nested
        @DisplayName("세번 호출되면,")
        class DescribeRacingGameRound3 {

            private final RacingGame racingGame = new RacingGame(cars);
            private final Car[] expectedCars = move();

            @Test
            @DisplayName("세판을 진행하고, 그 결과를 car에 반영한다.")
            void ItReturn1RoundResult() {
                racingGame.play();
                racingGame.play();
                racingGame.play();

                assertEqualAllCars(cars, expectedCars);
            }

            private Car[] move() {
                Car[] copy = {
                    new Car("A", falseMover),
                    new Car("B", falseMover),
                    new Car("C", trueMover),
                };
                for (Car car : copy) {
                    car.move();
                    car.move();
                    car.move();
                }
                return copy;
            }

        }

    }

    @Nested
    @DisplayName("getRoundResult 메소드는")
    class ContextRoundResultMethod {

        private final Car[] cars = {
            new Car("A", falseMover),
            new Car("B", falseMover),
            new Car("C", trueMover),
        };

        @Nested
        @DisplayName("play가 실행된 이후 호출하면,")
        class DescribeCall {

            private final RacingGame racingGame = new RacingGame(cars);

            private final Car[] expectedCars = move();
            @Test
            @DisplayName("현재 라운드의 결과를 반환한다.")
            void ItReturnRoundResult() {
                racingGame.play();
                Car[] roundResult = racingGame.getRoundResult();
                assertEqualAllCars(roundResult, expectedCars);
            }

            private Car[] move() {
                Car[] copy = {
                    new Car("A", falseMover),
                    new Car("B", falseMover),
                    new Car("C", trueMover),
                };
                for (Car car : copy) {
                    car.move();
                }
                return copy;
            }

        }

        @Nested
        @DisplayName("play가 두번 호출된 후, 호출되면")
        class DescribeCallBeforePlayTwice {

            private final RacingGame racingGame = new RacingGame(cars);

            private final Car[] expectedCars = move();

            @Test
            @DisplayName("두번째 라운드의 결과를 반환한다.")
            void ReturnTwiceRoundResult() {
                racingGame.play();
                racingGame.play();
                Car[] roundResult = racingGame.getRoundResult();
                assertEqualAllCars(roundResult, expectedCars);
            }

            private Car[] move() {
                Car[] copy = {
                    new Car("A", falseMover),
                    new Car("B", falseMover),
                    new Car("C", trueMover),
                };
                for (Car car : copy) {
                    car.move();
                    car.move();
                }
                return copy;
            }

        }

    }

    @Nested
    @DisplayName("getWinners 메소드는")
    class ContextGetWinnersMethod {

        private final Car[] cars = {
            new Car("win1", trueMover),
            new Car("win2", trueMover),
            new Car("loser", falseMover),
        };

        @Nested
        @DisplayName("한판 진행 후 호출되면,")
        class DescribeCall {

            private final RacingGame racingGame = new RacingGame(cars);

            private final Car[] expectedCars = getExpectedCars();
            @Test
            @DisplayName("우승자들을 반환한다.")
            void ItReturnRoundResult() {
                racingGame.play();
                Car[] roundResult = racingGame.getWinners();
                assertEqualAllCars(roundResult, expectedCars);
            }

            private Car[] getExpectedCars() {
                Car[] copy = {
                    new Car("win1", trueMover),
                    new Car("win2", trueMover),
                };
                for (Car car : copy) {
                    car.move();
                }
                return copy;
            }

        }

    }

    private void assertEqualAllCars(Car[] result, Car[] expected) {
        for (int i = 0; i < result.length; i++) {
            Assertions.assertThat(result[i]).isEqualTo(expected[i]);
        }
    }
}
