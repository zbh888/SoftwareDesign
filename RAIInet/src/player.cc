#include "player.h"

Player::~Player() {
    delete td;
    player_list.clear();
}

void Player::notify(Subject<Info, State> &whoNotified) {
    Info io = whoNotified.getInfo();
    State s = whoNotified.getState();
    if (s.stype == StateType::Init) {
        Info &iio = namecheck(io.name);
        iio.type = io.type;
        iio.strength = io.strength;
    }
    if (s.stype == StateType::Add) {
        td->add(io.row, io.col, io.name);
    }
    if (s.stype == StateType::Leave) {
        if ((s.downloader == 4) && (s.showsto == turn)) {
            td->setEmp(io.row, io.col, s.downloader);
        } else {
            td->setEmp(io.row, io.col, -1);
        }
    }
    if (s.stype == StateType::Downloaded) {
        Info &ioo = namecheck(io.name);
        ioo.visibleData = true;
    }
    if (s.stype == StateType::FireWall) {
        if (s.showsto != turn) {}
        else {
            td->add(io.row, io.col, '#');
        }
    }
    if (s.stype == StateType::Show) {
        if (turn != s.showsto) {}
        else {
            Info &iio = namecheck(io.name);
            iio.visibleData = true;
            iio.visibleStrength = true;
        }
    }
    if (s.stype == StateType::Reverse) {
        Info &iio = namecheck(io.name);
        if (iio.type == Type::Data) { iio.type = Type::Virus; }
        else if (iio.type == Type::Virus) { iio.type = Type::Data; }
    }
}

Info &Player::namecheck(char nam) {
    int num = player_list.size();
    int i = 0;
    int j = 0;
    while (i < num) {
        while (j < 8) {
            if (player_list[i][j].name == nam) { return player_list[i][j]; }
            j += 1;
        }
        j = 0;
        i += 1;
    }
}

Player::Player(int t, int playnum) : stillin{true}, turn{t} {
    td = new TextDisplay(playnum);
    int i;
    std::vector <Info> p;
    for (i = 0; i < 8; i++) {
        Info io;
        io.row = -1;
        io.col = -1;
        if (i < 4) {
            io.type = Type::Virus;
            io.strength = i + 1;
        }
        if (i > 3) {
            io.type = Type::Data;
            io.strength = i - 3;
        }
        p.push_back(io);
    }
    for (i = 0; i < 8; i++) { p[i].name = player1_name[i]; }
    player_list.push_back(p);
    for (i = 0; i < 8; i++) { p[i].name = player2_name[i]; }
    player_list.push_back(p);
    if (playnum == 4) {
        for (i = 0; i < 8; i++) { p[i].name = player3_name[i]; }
        player_list.push_back(p);
        for (i = 0; i < 8; i++) { p[i].name = player4_name[i]; }
        player_list.push_back(p);
    }
    i = 0;
    while (i < 8) {
        player_list[turn][i].visibleData = true;
        player_list[turn][i].visibleStrength = true;
        i += 1;
    }
}

bool Player::find(char name) {
    int i = 0;
    while (i < player_list[turn].size()) {
        if (player_list[turn][i].name == name) {
            return true;
        }
        i += 1;
    }
    return false;
}

void Player::print(int i) {
    int size = player_list[i].size();
    int j = 0;
    while (j < size) {
        std::cout << player_list[i][j].name << ": ";
        if (player_list[i][j].visibleData == false) { std::cout << "?"; }
        else {
            if (player_list[i][j].type == Type::Data) { std::cout << "D"; }
            if (player_list[i][j].type == Type::Virus) { std::cout << "V"; }
        }
        if (player_list[i][j].visibleStrength == false) { std::cout << "?"; }
        else { std::cout << player_list[i][j].strength; }
        if (j == (size / 2) - 1 || j == size - 1) { std::cout << std::endl; }
        else { std::cout << "  "; }
        j += 1;
    }
}

void Player::textdisplay() {
    td->printt();
}

void Player::setlose() {
    if (stillin == true) {
        std::cout << "###### Player " << turn + 1 << " loses! ######"
                  << std::endl;
        std::cout << "###### Player " << turn + 1 << " 's links and ports" <<
                  " are gone. ######" << std::endl;
        stillin = false;
    } else { stillin = false; }
}

bool Player::getstillin() { return stillin; }

void Player::deletewall(int i, int j) {
    td->add(i, j, '.');
}

std::vector<char> Player::getVisible_list() {
    std::vector<char> visible_list;
    for (int i = 0; i < player_list.size(); i++) {
        for (auto &inf : player_list[i]) {
            if (inf.visibleData) {
                visible_list.emplace_back(inf.name);
            }
        }
    }
    return visible_list;
}
