package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    public static void main (final String[] args) {

        Scanner sc = new Scanner(System.in);

        // 변수
        System.out.print("판 사이즈를 입력하세요 : ");
        int boardSize = Integer.parseInt(sc.nextLine());
        System.out.print("지뢰의 갯수를 입력하세요 : ");
        int mineSize = Integer.parseInt(sc.nextLine());

        // 판 생성
        Square[][] board = new Square[boardSize][boardSize];
        boolean gameOver;
        String restart = null;

        do {    // 게임 재시작
            try {
                // 게임생성
                makeboard(board, mineSize);
                // 게임설명 print
                printDescription(boardSize, mineSize);
                gameOver = false;
            } catch (Exception e) {
                System.out.println("(!) 게임 생성 중 오류가 발생하였습니다..");
                e.printStackTrace();
                gameOver = true;
            }


            while (!gameOver) { // 오픈할 칸 입력
                try {
                    System.out.println("오픈할 칸 입력 ( X , Y ) :                             ... 종료하기 : e");
                    String input = sc.nextLine();
                    if ("e".equals(input.trim())) {
                        System.out.println("게임 종료..");
                        gameOver = true;
                    }
                    if (!input.contains(",")) {
                        System.out.println("값을 ( X , Y ) 형태로 입력해주세요");
                        continue;
                    }

                    int x = Integer.parseInt(input.split(",")[0].replace("(", "").trim()) - 1;
                    int y = Integer.parseInt(input.split(",")[1].replace(")", "").trim()) - 1;

                    if (x < 0 || x > 9 || y < 0 || y > 9) {
                        System.out.println("X, Y 값은 1 ~ 10 사이의 값으로 입력해주세요.");
                        continue;
                    }

                    // 입력받은 칸 오픈
                    openBox(board, x, y);


                } catch (IllegalAccessException e) {
                    System.out.println("Game over!!!");
                    printAll(board);
                    System.out.print("게임을 다시 진행하시겠습니까?(Y/N) ");
                    restart = sc.nextLine().trim();
                    gameOver = true;
                } catch (Exception e) {
                    System.out.println("(!) 게임 진행 중 오류가 발생하였습니다..");
                    e.printStackTrace();
                    gameOver = true;
                }
            }
        } while ("Y".equals(restart));
        System.exit(0);

    }

    /**
     * 판 생성
     *  - 지뢰 설정
     *  - 주변 지뢰 갯수 설정
     */
    public static Square[][] makeboard (final Square[][] board, final int mineSize) {
        int boardSize = board.length;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] =  Square.of(i, j);
            }
        }

        int cnt = 0;
        while (cnt < mineSize) {
            int x = getRandomNumber(mineSize);    // 난수 생성 0 ~ boardSize-1
            int y = getRandomNumber(mineSize);    // 난수 생성 0 ~ boardSize-1

            if (!board[x][y].isMine()) {
                board[x][y].putMine(); // 지뢰 설정

                // 생성된 지뢰를 기준으로 주변 지뢰 수 계산
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        // 대상 검증 : 사방 8개 칸 중 지뢰가 아니고 사각형(board) 내 범위에 있는 칸 검증
                        if (i < 0 || j < 0 || boardSize <= i || boardSize <= j) {
                            continue;
                        }
                        if (i == x && j == y) {
                            continue;
                        }
                        if (board[i][j].isMine()) {
                            continue;
                        }
                        // 주변지뢰수 값 + 1
                        board[i][j].hasNearMine();
                    }
                }
                cnt++;
            }
        }

        return board;
    }

    private static int getRandomNumber (final int mineSize) {
        Random random = new Random();
        return random.nextInt(mineSize);
    }

    /**
     * 한 칸 열기(입력받은 칸 열기)
     */
    public static void openBox (Square[][] board, final int x, int y) throws IllegalAccessException {
        if (!board[y][x].isOpen()) {
            board[y][x].open();
        }
        if (board[y][x].isMine()) {
            throw new IllegalAccessException();
        } else if (0 == board[y][x].getMineNum()) {
            openEmptyBox(board, x, y);
        }
        printOpenBox(board);
    }


    /**
     * 한 칸 열기(입력받은 칸 열기)
     */
    public static void printOpenBox (Square[][] board) {
        for (Square[] col : board) {
            int lastRow = 0;
            for (Square row : col) {
                if (row.isOpen()) {
                    if (row.isMine()) {
                        System.out.print(" B ");
                    } else {
                        System.out.print(" " + row.getMineNum() + " ");
                    }
                } else {
                    System.out.print(" + ");
                }

                if (++lastRow == col.length) {
                    System.out.println();
                }
            }
        }

    }

    /**
     * 모든 칸 열기(Game over 시)
     */
    public static void printAll (final Square[][] board) {
        for (Square[] col : board) {
            int lastRow = 0;
            for (Square row : col) {
                if (row.isMine()) {
                    System.out.print(" B ");
                } else if (row.isOpen()) {
                    System.out.print(" " + row.getMineNum() + " ");
                } else {
                    System.out.print(" + ");
                }

                if (++lastRow == col.length) {
                    System.out.println();
                }
            }
        }

    }

    /**
     * 빈 칸 열기(주변 모든 칸에 지뢰 없는 경우)
     */
    public static void openEmptyBox (Square[][] board, final int x, final int y) {
        int boardSize = board.length;
        // 빈 칸을 기준으로 주변 칸 검증
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                // 대상 검증 : 사방 8개 칸 중 사각형(board) 내 범위에 있는 칸 모두 비어 있는 경우 열기
                if (i < 0 || j < 0 || boardSize <= i || boardSize <= j) {
                    continue;
                }
                if (i == x && j == y) {
                    continue;
                }
                if (!board[j][i].isOpen()) {
                    board[j][i].open();
                    if (0 == board[j][i].getMineNum()) {
                        openEmptyBox(board, i, j);
                    }
                }
            }
        }
        return;
    }

    /**
     * 게임 설명
    **/
    public static void printDescription (int boardSize, int mineSize) {
        System.out.println("");
        System.out.println("***** 게임 설명 *****");
        System.out.println("판 사이즈 : " + boardSize + "*" + boardSize + ", 지뢰 갯수 " + mineSize);
        System.out.println("");
        System.out.println("[+] : 열지 않은상태");
        System.out.println("[B] : 지뢰");
        System.out.println("[숫자] : 자신을 제외한 주변 칸 8개 중 지뢰의 갯수");
        System.out.println("");
        System.out.println(">>> Start!!!");
    }

}
