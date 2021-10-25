#include "publicinfo.h"

void PublicInfo::notify(Subject<Info, State> &whoNotified) {
    Info io = whoNotified.getInfo();
    State s = whoNotified.getState();
    if (s.stype == StateType::Downloaded) {
        if (turn != s.downloader) {}
        else {
            if (io.type == Type::Data) { Download_D += 1; }
            if (io.type == Type::Virus) { Download_V += 1; }
        }
    }
}

PublicInfo::PublicInfo(int t) : turn{t}, Download_V{0}, Download_D{0} {
    int i = 0;
    while (i < TotalAbility) {
        ability_list.push_back(0);
        i += 1;
    }
}

void PublicInfo::clearability() {
    int i = 0;
    while (i < ability_list.size()) {
        ability_list[i] = 0;
        i += 1;
    }
}

void PublicInfo::hero() {
    Download_V -= 1;
}

void PublicInfo::setability(std::string s) {
    clearability();
    std::stringstream ss;
    ss.str(s);
    char c;
    while (ss >> c) {
        if (c == 'L') { ability_list[0] += 1; }
        if (c == 'F') { ability_list[1] += 1; }
        if (c == 'D') { ability_list[2] += 1; }
        if (c == 'P') { ability_list[3] += 1; }
        if (c == 'S') { ability_list[4] += 1; }
        if (c == 'V') { ability_list[5] += 1; }
        if (c == 'H') { ability_list[6] += 1; }
        if (c == 'B') { ability_list[7] += 1; }
    }
}


void PublicInfo::print() {
    std::cout << "Downloaded: " << getData() << "D, " << getVirus() << "V" << std::endl;
    std::cout << "Abilities: " << totalability() << std::endl;
}

int PublicInfo::getData() { return Download_D; }

int PublicInfo::getVirus() { return Download_V; }

int PublicInfo::getnumability(int Id) { return ability_list[Id]; }

bool PublicInfo::canuse(int Id) {
    if (ability_list[Id] > 0) {
        ability_list[Id] -= 1;
        return true;
    } else {
        return false;
    }
}

PublicInfo::~PublicInfo() {
    ability_list.clear();
}

int PublicInfo::totalability() {
    int num = 0;
    int i = 0;
    while (i < ability_list.size()) {
        num += ability_list[i];
        i += 1;
    }
    return num;
}
