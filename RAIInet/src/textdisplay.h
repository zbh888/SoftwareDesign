#ifndef TEXTDISPLAY_H
#define TEXTDISPLAY_H
#include <iostream>
#include <vector>
#include "info.h"
class Link;

class TextDisplay {
  std::vector<std::vector<char>> theDisplay;
  const int playernum;
 public:
    TextDisplay(int kk);
    void add(int i, int j, char n);
    void setEmp(int i, int j, int wall);
    void printt();
};
#endif
