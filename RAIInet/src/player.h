#ifndef PLAYER_H
#define PLAYER_H
#include "subject.h"
#include "observer.h"
#include "textdisplay.h"
#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include "link.h"
class Player: public Observer<Info, State> {
    std::vector<std::vector<Info>> player_list;
    TextDisplay *td = nullptr;
    bool stillin;
    int turn;
public:
    ~Player();
    void notify(Subject<Info, State> &whoNotified);
    Info& namecheck(char nam);
    void deletewall(int i, int j);
    Player(int t, int playnum);
    bool find(char name);
    void print(int i);
    void textdisplay();
    void setlose();
    bool getstillin();
    std::vector<char> getVisible_list();
};

#endif
