#ifndef __INFO_H__
#define __INFO_H__
#include <cstddef>
#include <vector>
const int TotalAbility=8;
const std::vector<char> player1_name{'a','b','c','d','e','f','g','h'};
const std::vector<char> player2_name{'A','B','C','D','E','F','G','H'};
const std::vector<char> player3_name{'i','j','k','l','m','n','o','p'};
const std::vector<char> player4_name{'I','J','K','L','M','N','O','P'};
enum class AbilityType {Empty, LinkBoost, Vengeance};
enum class StateType {FireWall, Add, Leave, Show, Downloaded, Init, Reverse};
enum class Type { Port, Virus, Data, Empty };
enum class Direction { Up, Down, Left, Right };
struct State {
    StateType stype;
    int downloader=-1;
    int showsto=-1;
};
struct Info {
    int row, col;
    Type type=Type::Empty;
    int strength=-1;
    char name='.';
    bool visibleData=false;
    bool visibleStrength=false;
};


#endif
