CXX = g++
CXXFLAGS = -Wall -MMD -g -Werror=vla -std=c++11
EXEC = RAIInet
OBJECTS = link.o textdisplay.o window.o graph.o publicinfo.o player.o grid.o main.o
DEPENDS = ${OBJECTS:.o=.d}

${EXEC}: ${OBJECTS}
	${CXX} ${CXXFLAG} ${OBJECTS} -o ${EXEC} -lX11 -L/usr/X11/lib -I/usr/X11/include

-include ${DEPENDS}
.PHONY: clean

clean:
	rm ${OBJECTS} ${EXEC}
