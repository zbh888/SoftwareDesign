#include "graph.h"

GraphicDisplay::GraphicDisplay(int n) : r{8}, numPlayer(n) {
    r = 8;
    c = 8;
    xr = 2;
    xc = 1;
    if (n == 4) {
        c += 2;
        r += 2;
        xc += 1;
    }
    uWidth = width / c;
    uHeight = height / r;
    diameter = uWidth - 4;
    xWidth = width / xc;
    xHeight = height / xr;
}

void GraphicDisplay::initial() {
    //reset to white board
    w.fillRectangle(0, 0, 500, 500, Xwindow::White);
    for (int i = 1; i < r; i++) {
        w.drawLine(0, i * height / r, width, i * height / r);
    }
    for (int j = 1; j < c; j++) {
        w.drawLine(j * width / c, 0, j * width / c, height);
    }

    if (numPlayer == 4) {
        w.fillRectangle(0, 0, uWidth + 1, uHeight + 1, Xwindow::Blue);
        w.fillRectangle(width - uWidth, height - uHeight, uWidth + 1, uHeight + 1, Xwindow::Blue);
        w.fillRectangle(0, height - uHeight, uWidth + 1, uHeight + 1, Xwindow::Blue);
        w.fillRectangle(width - uWidth, 0, uWidth + 1, uHeight + 1, Xwindow::Blue);
    }
}


void GraphicDisplay::drawCircle(int i, int j, char n) {
    std::string s(1, n);
    w.fillCircle(uWidth * i + uWidth / 2, uHeight * j + uHeight / 2, diameter);
    w.drawString(uWidth * i + uWidth / 2 - 4, uHeight * j + uHeight / 2 + 4, s, Xwindow::White);
}


void GraphicDisplay::addInfo(int i, int j, char n, Info info) {
    std::string s;
    s += n;
    if (!info.visibleStrength) {
        s += std::to_string(info.strength);
    }
    if (info.type == Type::Data) {
        w.fillCircle(uWidth * i + uWidth / 2, uHeight * j + uHeight / 2, diameter, Xwindow::Green);
        w.drawString(uWidth * i + uWidth / 2 - 4, uHeight * j + uHeight / 2 + 4, s);
    }
    if (info.type == Type::Virus) {
        w.fillCircle(uWidth * i + uWidth / 2, uHeight * j + uHeight / 2, diameter, Xwindow::Red);
        w.drawString(uWidth * i + uWidth / 2 - 4, uHeight * j + uHeight / 2 + 4, s);
    }
    if (info.type == Type::Port) {
        w.fillCircle(uWidth * i + uWidth / 2, uHeight * j + uHeight / 2, diameter, Xwindow::Yellow);
    }
}

void GraphicDisplay::drawFireWall(int i, int j) {
    w.fillRectangle(uWidth * i, uHeight * j, uWidth + 1, uHeight + 1, Xwindow::Brown);
}

GraphicDisplay::~GraphicDisplay() {}

void GraphicDisplay::drawPort(int i, int j) {
    w.fillCircle(uWidth * i + uWidth / 2, uHeight * j + uHeight / 2, diameter, Xwindow::Yellow);
}


void GraphicDisplay::addPublic(PublicInfo *pubInfo, int turn) {
    std::string sd;
    std::string sa;
    int d = pubInfo->getData();
    int v = pubInfo->getVirus();
    int ab = pubInfo->totalability();
    sd += "Downloaded: ";
    sd += std::to_string(d);
    sd += "D, ";
    sd += std::to_string(v);
    sd += 'V';
    sa += "Abilities: ";
    sa += std::to_string(ab);
    switch (turn) {
        case 0:
            x.fillRectangle(10, 10, xWidth - 20, xHeight - 20, Xwindow::White);
            x.drawString(xWidth / 2 - 50, xHeight / 5, "Player1:");
            //draw Downloaded
            x.drawString(xWidth / 2 - 50, 2 * xHeight / 5, sd);
            //draw Ability
            x.drawString(xWidth / 2 - 50, 3 * xHeight / 5, sa);
            break;

        case 1:
            if (numPlayer == 4) {
                x.fillRectangle(xWidth + 10, 10, xWidth - 10, xHeight - 10, Xwindow::White);
                x.drawString(3 * xWidth / 2 - 50, xHeight / 5, "Player2:");
                x.drawString(3 * xWidth / 2 - 50, 2 * xHeight / 5, sd);
                x.drawString(3 * xWidth / 2 - 50, 3 * xHeight / 5, sa);
            }
            if (numPlayer == 2) {
                x.fillRectangle(10, xHeight + 10, xWidth - 10, xHeight - 10, Xwindow::White);
                x.drawString(width / 2 - 50, height / 2 + xHeight / 5, "Player2:");
                x.drawString(width / 2 - 50, height / 2 + 2 * xHeight / 5, sd);
                x.drawString(width / 2 - 50, height / 2 + 3 * xHeight / 5, sa);
            }
            break;

        case 2:
            x.fillRectangle(10, xHeight + 10, xWidth - 10, xHeight - 10, Xwindow::White);
            x.drawString(xWidth / 2 - 50, height / 2 + xHeight / 5, "Player3:");
            x.drawString(xWidth / 2 - 50, height / 2 + 2 * xHeight / 5, sd);
            x.drawString(xWidth / 2 - 50, height / 2 + 3 * xHeight / 5, sa);
            break;

        case 3:
            x.fillRectangle(xWidth + 10, xHeight + 10, xWidth - 10, xHeight - 10, Xwindow::White);
            x.drawString(3 * xWidth / 2 - 50, height / 2 + xHeight / 5, "Player4:");
            x.drawString(3 * xWidth / 2 - 50, height / 2 + 2 * xHeight / 5, sd);
            x.drawString(3 * xWidth / 2 - 50, height / 2 + 3 * xHeight / 5, sa);
            break;
    }
}

void GraphicDisplay::initPublic() {
    x.drawLine(0, xHeight, width, xHeight);
    if (numPlayer == 2) {
        x.drawString(width / 2 - 50, xHeight / 5, "Player1:");
        x.drawString(width / 2 - 50, height / 2 + xHeight / 5, "Player2:");
    }
    if (numPlayer == 4) {
        x.drawLine(xWidth, 0, xWidth, height);
        x.drawString(xWidth / 2 - 50, xHeight / 5, "Player1:");
        x.drawString(3 * uWidth / 2 - 50, xHeight / 5, "Player2:");
        x.drawString(xWidth / 2 - 50, height / 2 + xHeight / 5, "Player3:");
        x.drawString(3 * xWidth / 2 - 50, height / 2 + xHeight / 5, "Player4:");
    }
}

void GraphicDisplay::printWinner(std::string s) {
    x.fillRectangle(0, 0, width, height, Xwindow::White);
    x.drawString(width / 2 - 50, height / 2 - 50, s);
}
