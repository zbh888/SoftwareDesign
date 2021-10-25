#include "textdisplay.h"

TextDisplay::TextDisplay(int kk) : playernum{kk} {
    int m = 0;
    int r = 8;
    int c = 8;
    int n = 0;
    if (kk == 4) {
        c += 2;
        r += 2;
        m += 1;
        n += 2;
    }
    int i = 0;
    int j = 0;
    while (i < r) {
        std::vector<char> vec;
        while (j < c) {
            vec.push_back('.');
            j++;
        }
        j = 0;
        theDisplay.push_back(vec);
        i++;
    }
    theDisplay[0][m + 0] = 'a';
    theDisplay[0][m + 1] = 'b';
    theDisplay[0][m + 2] = 'c';
    theDisplay[0][m + 3] = 'S';
    theDisplay[0][m + 4] = 'S';
    theDisplay[1][m + 3] = 'd';
    theDisplay[1][m + 4] = 'e';
    theDisplay[0][m + 5] = 'f';
    theDisplay[0][m + 6] = 'g';
    theDisplay[0][m + 7] = 'h';
    theDisplay[n + 7][m + 0] = 'A';
    theDisplay[n + 7][m + 1] = 'B';
    theDisplay[n + 7][m + 2] = 'C';
    theDisplay[n + 7][m + 3] = 'S';
    theDisplay[n + 7][m + 4] = 'S';
    theDisplay[n + 6][m + 3] = 'D';
    theDisplay[n + 6][m + 4] = 'E';
    theDisplay[n + 7][m + 5] = 'F';
    theDisplay[n + 7][m + 6] = 'G';
    theDisplay[n + 7][m + 7] = 'H';
    if (kk == 4) {
        theDisplay[0][0] = '#';
        theDisplay[0][9] = '#';
        theDisplay[9][0] = '#';
        theDisplay[9][9] = '#';
        theDisplay[m + 0][0] = 'i';
        theDisplay[m + 1][0] = 'j';
        theDisplay[m + 2][0] = 'k';
        theDisplay[m + 3][0] = 'S';
        theDisplay[m + 4][0] = 'S';
        theDisplay[m + 3][1] = 'l';
        theDisplay[m + 4][1] = 'm';
        theDisplay[m + 5][0] = 'n';
        theDisplay[m + 6][0] = 'o';
        theDisplay[m + 7][0] = 'p';
        theDisplay[m + 0][9] = 'I';
        theDisplay[m + 1][9] = 'J';
        theDisplay[m + 2][9] = 'K';
        theDisplay[m + 3][9] = 'S';
        theDisplay[m + 4][9] = 'S';
        theDisplay[m + 3][8] = 'L';
        theDisplay[m + 4][8] = 'M';
        theDisplay[m + 5][9] = 'N';
        theDisplay[m + 6][9] = 'O';
        theDisplay[m + 7][9] = 'P';
    }

}

void TextDisplay::add(int i, int j, char n) {
    theDisplay[i][j] = n;
}

void TextDisplay::setEmp(int i, int j, int wall) {
    if (wall == 4) { theDisplay[i][j] = '#'; }
    else { theDisplay[i][j] = '.'; }
}

void TextDisplay::printt() {
    int i = 0;
    int j = 0;
    int r = theDisplay.size();
    int c = theDisplay[0].size();
    while (i < r) {
        while (j < c) {
            std::cout << theDisplay[i][j];
            if (j == (c - 1)) { std::cout << std::endl; }
            j += 1;
        }
        j = 0;
        i += 1;
    }
}
