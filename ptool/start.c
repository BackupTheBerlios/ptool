
#include <string.h>

// kurzes Startprogramm für Windows (läuft auch unter Linux, da aber lieber die Shell ;-)
// ToDo: damit es wieder unter Linux laueft, muss gegen javaw geprueft werden

int main(int para_anz, char* paras[]) {

char* cmd;
char* tmp="javaw -jar PTool.jar ";
char* tmp2="javaw -jar PTool.jar ";

cmd=(char*) malloc(sizeof(char)*strlen(tmp)+20);
cmd=strcpy(cmd,tmp);

if (strcmp(paras[1],"-v")==0) {
        printf("hallo");
}

if (para_anz > 1) {
        cmd=strcat(cmd,paras[1]);
}

if (!system(cmd)) {
        printf("Fehler!");
}
//printf(cmd);
//printf("\n");

return 0;
}

