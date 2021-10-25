#include "link.h"

Link::Link(Info io) {
    info = io;
    owner = -1;
    buff = AbilityType::Empty;
    firewallowner = -1;
}

Link::~Link() {}

void Link::setbuff(AbilityType b) { buff = b; }

Info Link::getInfo() const { return info; }

void Link::setInfo(Info inf) { info = inf; }

void Link::set(char name, int o, int strength, Type t) {
    info.name = name;
    owner = o;
    info.strength = strength;
    info.type = t;
}

void Link::setlink(char c, int strength) {
    if (c == 'D') { info.type = Type::Data; }
    else if (c == 'V') { info.type = Type::Virus; }
    else { throw (-1); }
    if ((strength > 4) || (strength < 1)) {
        throw (-1);
    } else {
        info.strength = strength;
    }
    State s;
    s.stype = StateType::Init;
    setState(s);
    notifyObservers();
}

void Link::setEmpty() {
    info.type = Type::Empty;
    info.name = '.';
    buff = AbilityType::Empty;
    owner = -1;
    info.strength = -1;
}

int Link::getStrength() { return info.strength; }

void Link::setowner(int i) { owner = i; }

int Link::getowner() { return owner; }

void Link::setfirewallowner(int i) { firewallowner = i; }

int Link::getfirewallowner() { return firewallowner; }

void Link::polarize() {
    if (info.type == Type::Data) {
        info.type = Type::Virus;
    } else if (info.type == Type::Virus) {
        info.type = Type::Data;
    }
}

AbilityType Link::getbuff() { return buff; }
