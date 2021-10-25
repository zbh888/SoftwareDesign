#ifndef LINK_H
#define LINK_H
#include <cstddef>
#include "subject.h"
#include "observer.h"
#include "info.h"
#include "player.h"
#include "publicinfo.h"
#include <iostream>
#include <vector>
#include <string>
#include <sstream>

class Link : public Subject<Info, State> {
    Info info;
    int owner;
    AbilityType buff; 
     int firewallowner;
 public:
     ~Link();
    Link(Info io);
    void setbuff(AbilityType b);
    AbilityType getbuff();
    Info getInfo() const override; 
    void setInfo(Info inf);
    void set(char name, int o, int strength, Type t);
    void setlink(char c, int strength);
    void setEmpty();
    int getStrength();
    void setowner(int i);
    int getowner();
    void setfirewallowner(int i);
    int getfirewallowner();
    void polarize();
};
#endif
