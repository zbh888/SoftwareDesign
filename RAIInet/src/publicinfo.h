#ifndef PUBLICINFO_H
#define PUBLICINFO_H
#include "subject.h"
#include "observer.h"
#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include "link.h"
class PublicInfo: public Observer<Info, State> {
    int Download_V;
    int Download_D;
    std::vector<int> ability_list;
    int turn;
public:
    ~PublicInfo();
    void notify(Subject<Info, State> &whoNotified);
    PublicInfo(int t);
    void clearability();
    void setability(std::string s);
    void hero();
    
    void print();
    int getData();
    int getVirus();
    int getnumability(int Id);
    
    bool canuse(int Id);
    int totalability();
};

#endif
