#include "grid.h"

Grid::~Grid() {
    theGrid.clear();
    int i = 0;
    while (i < playernum) {
        delete player_list[i];
        delete public_list[i];
        i += 1;
    }
    player_list.clear();
    public_list.clear();
    delete gd;
}

void Grid::toDownload(int i, int j, int loader) {
    State s;
    s.stype = StateType::Downloaded;
    s.downloader = loader;
    theGrid[i][j].setState(s);
    theGrid[i][j].notifyObservers();
}

void Grid::toLeave(int i, int j) {
    State s;
    s.stype = StateType::Leave;
    if (theGrid[i][j].getfirewallowner() == -1) {}
    else {
        s.showsto = theGrid[i][j].getfirewallowner();
        s.downloader = 4;
    }
    theGrid[i][j].setState(s);
    theGrid[i][j].notifyObservers();
    theGrid[i][j].setEmpty();
}

void Grid::toAdd(int i, int j) {
    State s;
    s.stype = StateType::Add;
    theGrid[i][j].setState(s);
    theGrid[i][j].notifyObservers();
}

void Grid::toShow(int i, int j, int shower) {
    State s;
    s.stype = StateType::Show;
    s.showsto = shower;
    theGrid[i][j].setState(s);
    theGrid[i][j].notifyObservers();
}

void Grid::toSetFire(int i, int j, int owner) {
    State s;
    s.stype = StateType::FireWall;
    int m = theGrid[i][j].getfirewallowner();
    if ((m >= 0) && (m <= 3)) {
        player_list[m]->deletewall(i, j);
    }
    theGrid[i][j].setfirewallowner(owner);
    s.showsto = owner;
    theGrid[i][j].setState(s);
    theGrid[i][j].notifyObservers();
}

void Grid::toReverse(int i, int j) {
    State s;
    s.stype = StateType::Reverse;
    theGrid[i][j].setState(s);
    theGrid[i][j].notifyObservers();
    theGrid[i][j].polarize();
}

void Grid::useability(int turn, bool &b, std::string s, int Id) { //使用ability
    int row = theGrid.size();
    int col = theGrid[0].size();
    std::stringstream ss;
    ss.str(s);
    if (s.length() == 1) {
        char cc;
        ss >> cc;
        int r, c;
        if (!locate(cc, r, c, row, col)) { throw (-1); }
        if (((Id == 2) && (theGrid[r][c].getowner() == turn)) ||
            (((Id == 5) || (Id == 0)) && (theGrid[r][c].getbuff() != AbilityType::Empty)) ||
            ((Id == 5) && ((theGrid[r][c].getStrength() != 1) || (theGrid[r][c].getowner() != turn))) ||
            ((Id == 6) && ((theGrid[r][c].getowner() != turn) || (public_list[turn]->getVirus() == 0) ||
                           (theGrid[r][c].getInfo().type != Type::Data) || (theGrid[r][c].getStrength() != 2))) ||
            ((Id == 7) && !(bomb(r, c, turn))) || //bomb
            (!public_list[turn]->canuse(Id))) {
            std::cout << "No, you can not use this ability!" << std::endl;
            throw (-1);
        }
        std::cout << "Using ability!!!" << std::endl;
        if (Id == 0) {
            theGrid[r][c].setbuff(AbilityType::LinkBoost);
            std::cout << "Get Faster!" << std::endl;
        } //boost
        if (Id == 2) {
            toShow(r, c, turn);
            toDownload(r, c, turn);
            toLeave(r, c);
            std::cout << "Hahaha, Download the link." << std::endl;
        } //Download
        if (Id == 3) {
            toReverse(r, c);
            std::cout << "Magic!" << std::endl;
        } //Polarize
        if (Id == 4) {
            toShow(r, c, turn);
            std::cout << "No more hide." << std::endl;
        } //Scan
        if (Id == 5) {
            theGrid[r][c].setbuff(AbilityType::Vengeance);
            std::cout << "You will not laugh at me anymore" << std::endl;
        } //Vangeance
        if (Id == 6) {
            int jj = (turn + 1) % playernum;
            toShow(r, c, jj);
            toDownload(r, c, jj);
            toLeave(r, c);
            public_list[turn]->hero();
            std::cout << "For the good of my service port." << std::endl;
        }//Hero
        if (Id == 7) {
            int kj = 0;
            while (kj < playernum) {
                if (kj != turn) { toShow(r, c, kj); }
                kj++;
            }
            toDownload(r, c, theGrid[r][c].getowner());
            toLeave(r, c);
            getbombed(r - 1, c - 1, row, col);
            getbombed(r + 1, c + 1, row, col);
            getbombed(r - 1, c + 1, row, col);
            getbombed(r + 1, c - 1, row, col);
            getbombed(r - 1, c, row, col);
            getbombed(r + 1, c, row, col);
            getbombed(r, c - 1, row, col);
            getbombed(r, c + 1, row, col);
            std::cout << "Bomb!" << std::endl;
        }
    } else {
        int all = string_to_number(s);
        int i;
        int j;
        if (all < 10) {
            i = 0;
            j = all;
        } else if (all > 9) {
            i = all / 10;
            j = all % 10;
        }
        if (Id == 1) {
            if ((theGrid[i][j].getInfo().type != Type::Empty) || ((playernum == 4) &&
                                                                  (((i == 0) && (j == 0)) || ((i == 9) && (j == 9)) ||
                                                                   ((i == 0) && (j == 9)) || ((i == 9) && (j == 0))))) {
                std::cout << "You cannot set firewall there." << std::endl;
                throw (-1);
            }
            if (!public_list[turn]->canuse(Id)) {
                std::cout << "No, you can not use this ability!" << std::endl;
                throw (-1);
            }
            toSetFire(i, j, turn);
            std::cout << "Virus, be careful. FireWall set." << std::endl;
        } else {
            std::cout << "please enter again." << std::endl;
            throw (-1);
        }
    }
    b = false;
}

bool Grid::toEdge(int i, int j, int turn, bool &load) {
    int row = theGrid.size();
    int col = theGrid[0].size();
    if ((0 < i && i < row - 1 && 0 < j && j < col - 1) ||
        (i == 0 && 0 < j && j < col - 1) ||
        (i == row - 1 && 0 < j && j < col - 1) ||
        (j == 0 && 0 < i && i < row - 1) ||
        (j == col - 1 && 0 < i && i < row - 1)) {
        return false;
    } else {
        if (playernum == 2) {
            if ((i == 0 && j == 0) || (i == 0 && j == col - 1) ||
                (i == row - 1 && j == 0) || (i == row - 1 && j == col - 1)) {
                return false;
            }
            if (turn == 0) {
                if (i >= row && 0 <= j && j < col) {
                    load = true;
                } else {
                    load = false;
                }
            } else {
                if (i < 0 && 0 <= j && j < col) {
                    load = true;
                } else {
                    load = false;
                }
            }

        } else if (playernum == 4) {
            if (turn == 0) {
                if ((i >= row && 0 < j && j < col - 1) || (j < 0 && 0 < i && i < row - 1) ||
                    (j >= row && 0 < i && i < row - 1)) {
                    load = true;
                } else {
                    load = false;
                }
            } else if (turn == 1) {
                if ((i < 0 && 0 < j && j < col - 1) || (j < 0 && 0 < i && i < row - 1) ||
                    (j >= row && 0 < i && i < row - 1)) {
                    load = true;
                } else {
                    load = false;
                }
            } else if (turn == 2) {
                if ((i < 0 && 0 < j && j < col - 1) || (i >= row && 0 < j && j < row - 1) ||
                    (j >= row && 0 < i && i < row - 1)) {
                    load = true;
                } else {
                    load = false;
                }
            } else {
                if ((i < 0 && 0 < j && j < col - 1) || (i >= row && 0 < j && j < row - 1) ||
                    (j < 0 && 0 < i && i < row - 1)) {
                    load = true;
                } else {
                    load = false;
                }
            }
        }
        return true;
    }
}


bool Grid::locate(char name, int &ii, int &jj, int row, int col) {
    int i = 0;
    int j = 0;
    while (i < row) {
        while (j < col) {
            if (theGrid[i][j].getInfo().name == name) {
                if (theGrid[i][j].getInfo().type == Type::Empty) {}
                else {
                    ii = theGrid[i][j].getInfo().row;
                    jj = theGrid[i][j].getInfo().col;
                    return true;
                }
            }
            j++;
        }
        j = 0;
        i++;
    }
    return false; //the name missing???????
}

void Grid::move(char name, Direction Dir, int turn) {
    int col = theGrid[0].size();
    int row = theGrid.size();
    int i, j, ii, jj;
    if (player_list[turn]->find(name)) { ///找到是他能移动的点
        if (!locate(name, i, j, row, col)) {
            std::cout << "You can't move." << std::endl;
            throw (-1); //确定位置坐标 是存在的
        }
        ii = i;
        jj = j;


        int distance = 1;
        if (theGrid[i][j].getbuff() == AbilityType::LinkBoost) { //有buff!
            distance += 1;
        }
        if (Dir == Direction::Up) { ii -= distance; }
        if (Dir == Direction::Down) { ii += distance; }
        if (Dir == Direction::Left) { jj -= distance; }
        if (Dir == Direction::Right) { jj += distance; }
        bool load;
        if (toEdge(ii, jj, turn, load)) { //离开边界 自己下载
            if (load) {
                std::cout << "Link says, I want to go back." << std::endl;
                toDownload(i, j, turn);
                toLeave(i, j);
            } else {
                std::cout << "That is a wall....." << std::endl;
                throw (-1);
            }
        } else if (theGrid[ii][jj].getInfo().type == Type::Empty) { //移动到空点
            if ((theGrid[ii][jj].getfirewallowner() != turn) &&
                (theGrid[ii][jj].getfirewallowner() != -1) &&
                (theGrid[i][j].getInfo().type == Type::Virus)) { //防火墙
                toDownload(i, j, theGrid[i][j].getowner());
                std::cout << "Hahaha, get your virus back home!" << std::endl;
                toLeave(i, j);
            } //没防火墙
            else {
                Info no = theGrid[i][j].getInfo();
                no.row = ii;
                no.col = jj;
                theGrid[ii][jj].setInfo(no);
                theGrid[ii][jj].setowner(theGrid[i][j].getowner());
                theGrid[ii][jj].setbuff(theGrid[i][j].getbuff());
                toAdd(ii, jj);
                toLeave(i, j);
            }
        } else { //对战！！！！
            if (theGrid[ii][jj].getowner() == turn) {
                std::cout << "You can't move upon your link." << std::endl;
                throw (-1);
            } else {
                if ((theGrid[ii][jj].getInfo().type == Type::Port) &&
                    (theGrid[i][j].getbuff() == AbilityType::LinkBoost)) {
                    std::cout << "You can't jump to their Port!" << std::endl;
                    throw (-1);
                }
                if ((theGrid[ii][jj].getfirewallowner() != -1) &&
                    (theGrid[ii][jj].getfirewallowner() != turn) &&
                    (theGrid[i][j].getInfo().type == Type::Virus)) {
                    toDownload(i, j, theGrid[i][j].getowner());
                    toLeave(i, j);
                } else {
                    toShow(i, j, theGrid[ii][jj].getowner());
                    if (theGrid[ii][jj].getInfo().type != Type::Port) {
                        toShow(ii, jj, theGrid[i][j].getowner());
                    } else {}
                    int a = theGrid[i][j].getStrength();
                    int b = theGrid[ii][jj].getStrength();
                    if (((a >= b) &&
                         (!((theGrid[ii][jj].getbuff() == AbilityType::Vengeance) && (b == 1) && (a == 4)))) ||
                        ((theGrid[i][j].getbuff() == AbilityType::Vengeance) && (b == 4) && (a == 1))) {
                        std::cout << "Player: " << turn + 1 <<
                                  " download the link: " << theGrid[ii][jj].getInfo().name;
                        std::cout << " !  It's a ";
                        if (theGrid[ii][jj].getInfo().type == Type::Data) {
                            std::cout << "Data!";
                        }
                        if (theGrid[ii][jj].getInfo().type == Type::Virus) {
                            std::cout << "Virus...";
                        }
                        std::cout << std::endl;
                        toDownload(ii, jj, turn);
                        toLeave(ii, jj);
                        Info no = theGrid[i][j].getInfo();
                        no.row = ii;
                        no.col = jj;
                        theGrid[ii][jj].setInfo(no);
                        theGrid[ii][jj].setowner(theGrid[i][j].getowner());
                        theGrid[ii][jj].setbuff(theGrid[i][j].getbuff());
                        toAdd(ii, jj);
                        toLeave(i, j);
                    } else if ((a < b) ||
                               ((theGrid[ii][jj].getbuff() == AbilityType::Vengeance) && (b == 1) && (a == 4))) {
                        std::cout << "Player: " << 1 + theGrid[ii][jj].getowner() <<
                                  " download the link: " << theGrid[i][j].getInfo().name;
                        std::cout << " !  It's a ";
                        if (theGrid[i][j].getInfo().type == Type::Data) {
                            std::cout << "Data!";
                        }
                        if (theGrid[i][j].getInfo().type == Type::Virus) {
                            std::cout << "Virus...";
                        }
                        std::cout << std::endl;
                        toDownload(i, j, theGrid[ii][jj].getowner());
                        toLeave(i, j);
                    }

                }

            }
        }

    } else {
        std::cout << "You can't move." << std::endl;
        throw (-1); //mei zhao dao
    }
}

void Grid::setplayerability(int turn, std::string s) {
    public_list[turn]->setability(s);
} //初始化每个选手的ability

void Grid::setlink(int turn, std::string sss) { //初始化link
    std::stringstream ss;
    ss.str(sss);
    int a, b, c, d, e, f, g, h;
    char A, B, C, D, E, F, G, H;
    ss >> A >> a >> B >> b >> C >> c >> D >> d >> E >> e >> F >> f >> G >> g >> H >> h;
    int m = 0;
    int n = 0;
    if (playernum == 4) {
        m += 1;
        n += 2;
    }
    if (turn == 0) {
        theGrid[0][m + 0].setlink(A, a);
        theGrid[0][m + 1].setlink(B, b);
        theGrid[0][m + 2].setlink(C, c);
        theGrid[1][m + 3].setlink(D, d);
        theGrid[1][m + 4].setlink(E, e);
        theGrid[0][m + 5].setlink(F, f);
        theGrid[0][m + 6].setlink(G, g);
        theGrid[0][m + 7].setlink(H, h);
    }
    if (turn == 1) {
        theGrid[n + 7][m + 0].setlink(A, a);
        theGrid[n + 7][m + 1].setlink(B, b);
        theGrid[n + 7][m + 2].setlink(C, c);
        theGrid[n + 6][m + 3].setlink(D, d);
        theGrid[n + 6][m + 4].setlink(E, e);
        theGrid[n + 7][m + 5].setlink(F, f);
        theGrid[n + 7][m + 6].setlink(G, g);
        theGrid[n + 7][m + 7].setlink(H, h);
    }


    if (turn == 2) {
        theGrid[m + 0][0].setlink(A, a);
        theGrid[m + 1][0].setlink(B, b);
        theGrid[m + 2][0].setlink(C, c);
        theGrid[m + 3][1].setlink(D, d);
        theGrid[m + 4][1].setlink(E, e);
        theGrid[m + 5][0].setlink(F, f);
        theGrid[m + 6][0].setlink(G, g);
        theGrid[m + 7][0].setlink(H, h);
    }

    if (turn == 3) {
        theGrid[m + 0][9].setlink(A, a);
        theGrid[m + 1][9].setlink(B, b);
        theGrid[m + 2][9].setlink(C, c);
        theGrid[m + 3][8].setlink(D, d);
        theGrid[m + 4][8].setlink(E, e);
        theGrid[m + 5][9].setlink(F, f);
        theGrid[m + 6][9].setlink(G, g);
        theGrid[m + 7][9].setlink(H, h);
    }


}

bool Grid::outt(int turn) { //判断选手被淘汰
    if (player_list[turn]->getstillin() == false) { return true; }
    if (public_list[turn]->getVirus() >= 4) {
        return true;
    }
    int i = 0;
    int j = 0;
    int num = 0;
    int row = theGrid.size();
    int col = theGrid[0].size();
    while (i < row) {
        while (j < col) {
            if (((theGrid[i][j].getInfo().type == Type::Data) ||
                 (theGrid[i][j].getInfo().type == Type::Virus)) &&
                (theGrid[i][j].getowner() == turn)) {
                num += 1;
            }
            j += 1;
        }
        j = 0;
        i += 1;
    }
    if (num == 0) { return true; }
    return false;
}

void Grid::lose() {
    int i = 0;
    int j = 0;
    int k = 0;
    int row = theGrid.size();
    int col = theGrid[0].size();
    while (i < playernum) {
        if (player_list[i]->getstillin() == false) {}
        else if (outt(i)) {
            player_list[i]->setlose();
            while (j < row) {
                while (k < col) {
                    if (theGrid[j][k].getowner() == i) {
                        toLeave(j, k);
                    }
                    k += 1;
                }
                k = 0;
                j += 1;
            }
        }
        i += 1;
    }
}

bool Grid::won(int &winner) { //判断出winner是谁
    int i = 0;
    int left = playernum;
    while (i < playernum) {
        if (!player_list[i]->getstillin()) { left -= 1; }
        else {
            if (public_list[i]->getData() >= 4) {
                winner = i + 1;
                return true;
            }
        }
        i += 1;
    }
    if (left == 1) {
        i = 0;
        while (i < playernum) {
            if (player_list[i]->getstillin()) {
                winner = i + 1;
            }
            i += 1;
        }
        return true;
    }
    return false;
}

void Grid::printability(int turn) {   // 1: Link Boost, 2 left
    int i = 0;                 // 2: Firewall, 3 left
    while (i < TotalAbility) {
        std::cout << i + 1 << ": ";
        if (i == 0) { std::cout << "Link Boost, "; }
        if (i == 1) { std::cout << "Firewall, "; }
        if (i == 2) { std::cout << "Download, "; }
        if (i == 3) { std::cout << "Polarize, "; }
        if (i == 4) { std::cout << "Scan, "; }
        if (i == 5) { std::cout << "Vengeance, "; }
        if (i == 6) { std::cout << "Hero, "; }
        if (i == 7) { std::cout << "Bomb, "; }
        std::cout << public_list[turn]->getnumability(i) << " left" << std::endl;
        i += 1;
    }
}

void Grid::gprint(int turn) {
    int i = 0;
    while (i < playernum) {
        std::cout << "Player " << i + 1 << ":" << std::endl;
        public_list[i]->print();
        player_list[turn]->print(i);
        if (i == (playernum / 2) - 1) {
            std::cout << "========";
            if (playernum == 4) { std::cout << "=="; }
            std::cout << std::endl;
            player_list[turn]->textdisplay();
            std::cout << "========";
            if (playernum == 4) { std::cout << "=="; }
            std::cout << std::endl;
        }
        i += 1;
    }
}

void Grid::init(int num) {
    playernum = num;
    int row = 8;
    int col = 8;
    if (playernum == 4) {
        col += 2;
        row += 2;
    }
    int i = 0;
    while (i < playernum) {
        PublicInfo *pp = new PublicInfo(i);
        pp->setability("LFDSP");
        public_list.push_back(pp);
        i += 1;
    }
    i = 0;
    while (i < playernum) {
        Player *p = new Player(i, playernum);
        player_list.push_back(p);
        i++;
    }
    i = 0;
    int j = 0;
    Info io;
    while (i < row) {
        std::vector <Link> vec;
        while (j < col) {
            io.row = i;
            io.col = j;
            Link l(io);
            for (int k = 0; k < playernum; k++) {
                l.attach(player_list[k]);
                l.attach(public_list[k]);
            }
            vec.push_back(l);
            j += 1;
        }
        j = 0;
        theGrid.push_back(vec);
        i += 1;
    }
    int m = 0;
    int n = 0;
    if (playernum == 4) {
        m += 1;
        n += 2;
    }
    theGrid[0][m + 0].set('a', 0, 1, Type::Virus);
    theGrid[0][m + 1].set('b', 0, 2, Type::Virus);
    theGrid[0][m + 2].set('c', 0, 3, Type::Virus);
    theGrid[0][m + 3].set('S', 0, 5, Type::Port);
    theGrid[0][m + 4].set('S', 0, 5, Type::Port);
    theGrid[0][m + 5].set('f', 0, 2, Type::Data);
    theGrid[0][m + 6].set('g', 0, 3, Type::Data);
    theGrid[0][m + 7].set('h', 0, 4, Type::Data);
    theGrid[1][m + 3].set('d', 0, 4, Type::Virus);
    theGrid[1][m + 4].set('e', 0, 1, Type::Data);

    theGrid[n + 7][m + 0].set('A', 1, 1, Type::Virus);
    theGrid[n + 7][m + 1].set('B', 1, 2, Type::Virus);
    theGrid[n + 7][m + 2].set('C', 1, 3, Type::Virus);
    theGrid[n + 7][m + 3].set('S', 1, 5, Type::Port);
    theGrid[n + 7][m + 4].set('S', 1, 5, Type::Port);
    theGrid[n + 7][m + 5].set('F', 1, 2, Type::Data);
    theGrid[n + 7][m + 6].set('G', 1, 3, Type::Data);
    theGrid[n + 7][m + 7].set('H', 1, 4, Type::Data);
    theGrid[n + 6][m + 3].set('D', 1, 4, Type::Virus);
    theGrid[n + 6][m + 4].set('E', 1, 1, Type::Data);
    if (m == 1) {
        theGrid[m + 0][0].set('i', 2, 1, Type::Virus);
        theGrid[m + 1][0].set('j', 2, 2, Type::Virus);
        theGrid[m + 2][0].set('k', 2, 3, Type::Virus);
        theGrid[m + 3][0].set('S', 2, 5, Type::Port);
        theGrid[m + 4][0].set('S', 2, 5, Type::Port);
        theGrid[m + 3][1].set('l', 2, 4, Type::Virus);
        theGrid[m + 4][1].set('m', 2, 1, Type::Data);
        theGrid[m + 5][0].set('n', 2, 2, Type::Data);
        theGrid[m + 6][0].set('o', 2, 3, Type::Data);
        theGrid[m + 7][0].set('p', 2, 4, Type::Data);
        theGrid[m + 0][9].set('I', 3, 1, Type::Virus);
        theGrid[m + 1][9].set('J', 3, 2, Type::Virus);
        theGrid[m + 2][9].set('K', 3, 3, Type::Virus);
        theGrid[m + 3][9].set('S', 3, 5, Type::Port);
        theGrid[m + 4][9].set('S', 3, 5, Type::Port);
        theGrid[m + 3][8].set('L', 3, 4, Type::Virus);
        theGrid[m + 4][8].set('M', 3, 1, Type::Data);
        theGrid[m + 5][9].set('N', 3, 2, Type::Data);
        theGrid[m + 6][9].set('O', 3, 3, Type::Data);
        theGrid[m + 7][9].set('P', 3, 4, Type::Data);
    }
}

int Grid::string_to_number(std::string s) {
    std::stringstream ss;
    int x;
    ss.str(s);
    ss >> x;
    return x;
}

void Grid::graph(int turn) {
    gd->initial();
    Info info;
    std::vector<char> visible = player_list[turn]->getVisible_list();
    for (int i = 0; i < playernum + 6; i++) {
        for (int j = 0; j < playernum + 6; j++) {
            info = theGrid[i][j].getInfo();
            if ((theGrid[i][j].getfirewallowner() != -1) &&
                (theGrid[i][j].getfirewallowner() == turn)) {
                gd->drawFireWall(j, i);
            }
            if (info.type != Type::Empty && info.type != Type::Port) {
                gd->drawCircle(j, i, info.name);
            }
            if (info.type == Type::Port) {
                gd->drawPort(j, i);
            }
        }
    }
    for (auto &name : visible) {
        for (auto &vec : theGrid) {
            for (auto &link : vec) {
                if (link.getInfo().name == name) {
                    gd->addInfo(link.getInfo().col, link.getInfo().row, name, link.getInfo());
                }
            }
        }
    }
    visible.clear();
}

void Grid::initGraph() {
    gd = new GraphicDisplay(playernum);
    gd->initPublic();
    for (int turn = 0; turn < playernum; turn++) {
        gd->addPublic(public_list[turn], turn);
    }
}


void Grid::graphAbility() {
    int i = 0;
    while (i < playernum) {
        gd->addPublic(public_list[i], i);
        i++;
    }
}

bool Grid::abilitydefault(std::string ssss) {
    if (ssss.length() != 5) { return false; }
    std::stringstream ss;
    ss.str(ssss);
    char c;
    while (ss >> c) {
        if (c == 'L') {}
        else if (c == 'F') {}
        else if (c == 'D') {}
        else if (c == 'P') {}
        else if (c == 'S') {}
        else if (c == 'V') {}
        else if (c == 'H') {}
        else if (c == 'B') {}
        else { return false; }
    }
    return true;
}

bool Grid::linkdefault(std::string ssss) {
    if (ssss.length() != 16) { return false; }
    std::stringstream ss;
    ss.str(ssss);
    int D = 0;
    int V = 0;
    char c;
    int a;
    int num_1 = 0;
    int num_2 = 0;
    int num_3 = 0;
    int num_4 = 0;
    while (ss >> c) {
        ss >> a;
        if (a == 1) { num_1 += 1; }
        if (a == 2) { num_2 += 1; }
        if (a == 3) { num_3 += 1; }
        if (a == 4) { num_4 += 1; }
        if ((c != 'D') && (c != 'V')) { return false; }
        if (c == 'D') { D += a; }
        if (c == 'V') { V += a; }
        if ((a < 1) || (a > 4)) { return false; }
    }
    if ((D != 10) || (V != 10)) { return false; }
    if ((num_1 != 2) || (num_2 != 2) || (num_3 != 2) || (num_4 != 2)) { return false; }
    return true;
}

bool Grid::bomb(int i, int j, int tur) {
    if ((theGrid[i][j].getInfo().type == Type::Empty) ||
        (theGrid[i][j].getInfo().type == Type::Data) ||
        (theGrid[i][j].getInfo().type == Type::Port)) { return false; }
    if (theGrid[i][j].getStrength() != 3) { return false; }
    if (theGrid[i][j].getowner() != tur) { return false; }
    return true;
}

void Grid::getbombed(int i, int j, int row, int col) {
    if ((i >= 0) && (i < row) && (j >= 0) && (j < col)) {
        if (theGrid[i][j].getInfo().type == Type::Virus) {
            toDownload(i, j, theGrid[i][j].getowner());
            toLeave(i, j);
        } else {}
    } else {}
}

void Grid::graphWinner(int i) {
    std::string s = "Congratulation! Player ";
    s += std::to_string(i);
    s += " wins the game!!";
    gd->printWinner(s);
}
