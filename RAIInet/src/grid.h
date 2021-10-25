#ifndef GRID_H
#define GRID_H
#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <cstddef>
#include "link.h"
#include "publicinfo.h"
#include "player.h"
#include "info.h"
#include "graph.h"
class TextDisplay;
template <typename InfoType, typename StateType> class Observer;
class InvalidMove{};

class Grid {
    std::vector<std::vector<Link>> theGrid;  // The actual grid.
    std::vector<Player*> player_list;
    std::vector<PublicInfo*> public_list;
    int playernum;
    GraphicDisplay* gd = nullptr;
 public:
    ~Grid();
    int string_to_number(std::string s);
    void useability(int turn, bool& b, std::string s, int Id);
    bool toEdge(int i, int j, int turn, bool& load);
    bool locate(char name, int& ii , int &jj, int row, int col);
    void move(char name, Direction Dir, int turn);
    void setplayerability(int turn, std::string s);
    void setlink(int turn, std::string sss);
    void graph(int turn);
    void graphAbility();
    bool bomb(int i, int j, int tur);
    void getbombed(int i, int j, int row, int col);
    void initGraph();
    bool outt(int turn);
    void lose();
    bool won(int& winner);
    void printability(int turn);
    void gprint(int turn);
    void toSetFire(int i, int j, int owner);
    void init(int num);
    void toDownload(int i, int j, int loader);
    void toLeave(int i, int j);
    void toAdd(int i, int j);
    void toShow(int i, int j ,int shower);
    void toReverse(int i, int j);
    bool abilitydefault(std::string ssss);
    bool linkdefault(std::string ssss);
    void graphWinner(int i);
};

#endif

