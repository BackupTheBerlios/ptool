#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <malloc.h>

/* kurzes Startprogramm für Windows
 * (läuft auch unter Linux, da aber lieber die Shell ;-)
 */
 
#ifdef LINUX
char* prog = "java -jar PSAgent.jar";
#else
char* prog = "javaw -jar PSAgent.jar";
#endif

// Prototypen
char* implode(const int len, char* array[]);



int main(int argc, char* argv[]) {

	// Parameter in einen durch Leerzeichen getrennten String wandeln
	char* para = implode(argc, argv);
	// String cmd mit entsprechend Speicher vorbelegen
	char* cmd = (char*) malloc(sizeof(char)*(strlen(prog)+strlen(para)));
	
	// cmd zusammensetzen
	strcpy(cmd, prog);
	strcat(cmd, para);

	if (!system(cmd)) {
        fprintf(stderr, "Fehler!");
	}
	
	//printf("%s\n",cmd);

	return 0;
}

char* implode(const int len, char* array[])
{
	char* result;
	char* space = " ";
	int i, mem = 0;
	
	// geht das nicht einfacher???
	for (i = 1; i < len; i++)
	{
		mem += strlen(array[i]);
	}

	result = (char*) malloc(sizeof(char) * mem);
	
	// Start bei 1, um Programmname zu übergehen
	for (i = 1; i < len; i++)
	{
		strcat(result, space);
		strcat(result, array[i]);
	}
	
	return result;
}
