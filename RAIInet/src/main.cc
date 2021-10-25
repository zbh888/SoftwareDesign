#include <iostream>
#include <fstream>
#include <string>
// You may include other allowed headers, as needed
#include "grid.h"
#include "info.h"
#include <vector>
#include <sstream>

using namespace std;

// Do not remove any code; do not add code other than where indicated.
int main(int argc, char *argv[]) {
    cin.exceptions(ios::eofbit | ios::failbit);
    string cmd;
    int graphhh = 0;
    int playernum;
    std::string mod(argv[1]);
    if (mod == "2") {
        cout << "           # 2-Player Set #" << endl;
        playernum = 2;
    }
    if (mod == "4") {
        cout << "           # 4-Player Set #" << endl;
        playernum = 4;
    }  //获取player的数量
    Grid g;
    g.init(playernum);  //新建棋盘 并建立很多连接
    int i = 2;
    while (i < argc) {
        string::size_type p;
        std::string sss = std::string(argv[i]);
        if ((sss.find("ability") != string::npos) && (sss.length() < 11)) {  //string::npos == -1;
            p = sss.find_first_of("0123456789");
            int pnum = g.string_to_number(sss.substr(p));
            i += 1;
            std::string ssss = std::string(argv[i]);
            if (g.abilitydefault(ssss)) {
                g.setplayerability(pnum - 1, ssss); //把player里的player 改ability
                std::cout << "# Player" << pnum << " abilities successfully set! #" << std::endl;
            } else { std::cout << "# Player" << pnum << " ability default #" << std::endl; }
        }
        if ((sss.find("link") != string::npos) && (sss.length() < 8)) {
            p = sss.find_first_of("0123456789");
            int pnum = g.string_to_number(sss.substr(p, p + 1));
            i += 1;
            std::string ssss = std::string(argv[i]);
            if (g.linkdefault(ssss)) {
                g.setlink(pnum - 1, ssss); //改link  link通知player 改info
                std::cout << "# Player" << pnum << " links successfully set! #" << std::endl;
            } else { std::cout << "# Player" << pnum << " links default #" << std::endl; }
        }
        if ((sss.find("graphics") != string::npos) && (sss.length() < 11)) {
            g.initGraph();//某种能打开图的方式
            graphhh += 1;
        }
        i += 1;
    }

    //  以上为初始化
    //  以下为进行游戏
    cout << "######## Game RAllnet Start! ########" << endl;
    cout << "$             Have fun!             $" << endl;
    cout << "######## Game RAllnet Start! ########" << endl << endl;
    cout << "~~~~~~ it's turn for Player: 1 ~~~~~~" << endl;
    int turn = 0;
    bool buff = true; //触发什么技能
    if (graphhh == 1) { g.graph(turn); }
    try {
        while (true) {
            turn = turn % playernum; //当前到了哪个player
            cin >> cmd;
            if (cmd == "quit") break;
            if (cmd == "move") {
                char name;
                string dir;
                cin >> name >> dir;
                Direction Dir;
                try {
                    if (dir == "up") { Dir = Direction::Up; }
                    else if (dir == "down") { Dir = Direction::Down; }
                    else if (dir == "left") { Dir = Direction::Left; }
                    else if (dir == "right") { Dir = Direction::Right; }
                    else {
                        cout << "A correct direction please" << endl;
                        throw (-1);
                    }
                    g.move(name, Dir, turn);
                    turn += 1;
                    turn = turn % playernum;
                    i = turn;
                    while (true) {
                        if (!g.outt(i)) {
                            break;
                        }
                        turn += 1;
                        turn = turn % playernum;
                        i++;
                        i = i % playernum;
                    }
                    cout << "~~~~~~ it's turn for Player: " << turn + 1 << " ~~~~~~" << std::endl;
                    if (graphhh == 1) {
                        g.graph(turn);
                        g.graphAbility();
                    }
                    buff = true;
                } //每一个棋子 通知了player里的信息
                catch (...) {} //如果违规 不进行任何操作
                g.lose();
                int winner;
                if (g.won(winner)) {
                    if (graphhh == 1) {
                        g.graph(turn);
                        g.graphWinner(winner);
                    }
                    cout << "~~ Congratulations!! ~~" << endl;
                    cout << "Player " << winner << " wins the game!" << endl;
                    cout << "~~ Congratulations!! ~~" << endl;
                    break;
                }
            }
            if (cmd == "abilities") {
                //调取当前player 的信息
                g.printability(turn);
            }
            if (cmd == "ability") {
                if (buff == false) {
                    cout << "You can't use ability!" << endl;
                } else {
                    int ID = -1;
                    cin >> ID;
                    string s;
                    cin >> s;
                    if (ID == 2) { //ability 0 1 3
                        string t;
                        cin >> t;
                        s += t;
                    }
                    ID -= 1;
                    if ((ID < 0) || (ID > 7)) {
                        std::cout << "We don't have such ability." << std::endl;
                    } else {
                        try {
                            g.useability(turn, buff, s, ID);
                            if (graphhh == 1) {
                                g.graph(turn);
                                g.graphAbility();
                            }
                        }
                        catch (...) {};
                    }
                }
            }
            if (cmd == "board") {
                g.gprint(turn);
            }
            if (cmd == "sequence") { //    Sequence  Sequence  Sequence  Sequence
                string file;
                cin >> file;
                string command_member;
                vector <string> v;
                ifstream cin(file);
                while (cin >> command_member) {
                    v.push_back(command_member);
                }
                int l = 0;
                try {
                    while (i < v.size()) {
                        turn = turn % playernum;
                        if (graphhh == 1) {
                            g.graph(turn);
                        }
                        if (v[l] == "quit") break;
                        if (v[l] == "move") {
                            l += 1;
                            char name;
                            string dir;
                            stringstream another;
                            another.str(v[l]);
                            another >> name;
                            l += 1;
                            another.str(v[l]);
                            another >> dir;
                            Direction Dir;
                            try {
                                if (dir == "up") { Dir = Direction::Up; }
                                else if (dir == "down") { Dir = Direction::Down; }
                                else if (dir == "left") { Dir = Direction::Left; }
                                else if (dir == "right") { Dir = Direction::Right; }
                                else {
                                    cout << "A correct direction please" << endl;
                                    throw (-1);
                                }
                                g.move(name, Dir, turn);
                                turn += 1;
                                turn = turn % playernum;
                                i = turn;
                                while (true) {
                                    if (!g.outt(i)) {
                                        break;
                                    }
                                    turn += 1;
                                    turn = turn % playernum;
                                    i++;
                                    i = i % playernum;
                                }
                                cout << "~~~~~~ it's turn for Player: " << turn + 1 << " ~~~~~~" << std::endl;
                                if (graphhh == 1) {
                                    g.graph(turn);
                                    g.graphAbility();
                                }
                                buff = true;
                            } //每一个棋子 通知了player里的信息
                            catch (...) {} //如果违规 不进行任何操作
                            g.lose();
                            int winner;
                            if (g.won(winner)) {
                                if (graphhh == 1) {
                                    g.graph(turn);
                                    g.graphWinner(winner);
                                }
                                cout << "~~ Congratulations!! ~~" << endl;
                                cout << "Player " << winner << " wins the game!" << endl;
                                cout << "~~ Congratulations!! ~~" << endl;
                                break;
                            }
                        }
                        //
                        if (v[l] == "abilities") {
                            //调取当前player 的信息
                            g.printability(turn);
                        }
                        //
                        if (v[l] == "ability") {
                            if (buff == false) {
                                cout << "You can't use ability!" << endl;
                            } else {
                                l += 1;
                                int ID = -1;
                                stringstream itsastream;
                                itsastream.str(v[l]);
                                itsastream >> ID;
                                l += 1;
                                string s = v[l];
                                if (ID == 2) { //ability 0 1 3
                                    l += 1;
                                    string t = v[l];
                                    s += t;
                                }
                                ID -= 1;
                                if ((ID < 0) || (ID > 7)) {
                                    std::cout << "We don't have such ability." << std::endl;
                                } else {
                                    try {
                                        g.useability(turn, buff, s, ID);
                                        if (graphhh == 1) {
                                            g.graph(turn);
                                            g.graphAbility();
                                        }
                                    }
                                    catch (...) {};
                                }
                            }
                        }
                        //
                        if (v[l] == "board") {
                            g.gprint(turn);
                        }


                        l += 1;
                    }
                }
                catch (ios::failure &) {}
                v.clear();
            }   //    Sequence  Sequence  Sequence  Sequence
        }
    }
    catch (ios::failure &) {}  // Any I/O failure quits
    return 0;
}
