#ifndef GRAPH_H
#define GRAPH_H
#include <iostream>
#include <vector>
#include "info.h"
#include "window.h"
#include "publicinfo.h"
#include <string>
class Link;

class GraphicDisplay {
	Xwindow w;
    Xwindow x;
    int width = 500;
    int height = 500;
    int diameter;
    int uWidth;
    int uHeight;
    int xWidth;
    int xHeight;
    int r;
    int c;
    int xr;
    int xc;
    int numPlayer;

public:
	void drawFireWall(int i, int j);
	GraphicDisplay(int n);
	void drawCircle(int i, int j, char n);
	void addInfo(int i, int j, char n, Info info);
	void initial();
    void drawPort(int i, int j);
        ~GraphicDisplay();
    void initPublic();
    void addPublic(PublicInfo* pubInfo, int turn);
    void printWinner(std::string s);
};



#endif

