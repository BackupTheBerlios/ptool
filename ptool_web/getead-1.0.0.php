<?
/* Schnittstelle DB <-> PTool (I-Daten auslesen)
 * by Enrico Tröger 2004
 * (inkl. Komprimierung)
 * Version 0.3
 * ToDo: Kats aus Config.php auslesen
*/


$key = file("./key");

$kategorie=array(
	"Rock",
	"Kultur",
	"Konzert",
	"Disco",
	"Punk",
	"Sonstige"
	);


// Komprimierungs-Algorithmus
function zippit($source) {
	return gzencode($source, 9);
}

// Verschlüsselungs-Algorithmus
function xor_encode($source,$key) {

	$coded="";
	for ($i = 0; $i < strlen($source); ++$i) {
		$coded.=($source[$i] ^ chr($key));
	}
	return $coded;
	//return $source;
}

// DB-Verbindung aufbauen
//require("/vpserver/web/vps02042/www/config.php");
require("config.php");
$conn = mysql_connect($host,$dbuser,$dbpwd);
if (!$conn) { die("1"); }
@mysql_select_db($dbname);


// User-Daten prüfen
if (($user=="" AND $pass=="") OR mysql_num_rows(mysql_query("SELECT id FROM m_profil WHERE (user='$user' AND pwd='$pass')",$conn))!=1) { 			die("2"); }


// Daten holen

$all="";
$plz="";

// DIE SQL-Abfrage (gilt für "Alle Kreise" und die jeweiligen Kreise)
$sql=mysql_query("SELECT plz,name FROM i_orte WHERE ok='J' order by name",$conn);
while (list($plz_tmp,$ort)=mysql_fetch_row($sql)) {
	$all.="$ort|";
	$plz.="$plz_tmp|";
}

$all.="a\n".$plz;

// durch das a wird in Java beim Splitten der Zeile ein letztes Feld mit Inhalt "a" erzeugt, welches
// dann durch Andere ersetzt wird
$all.="a\n";
$sql=mysql_query("SELECT band FROM i_bands order by band",$conn);
while (list($band)=mysql_fetch_row($sql)) {
	$all.="$band|";
}

$all.="a|a\n";
$sql=mysql_query("SELECT orga_name FROM i_orga order by orga_name",$conn);
while (list($orga)=mysql_fetch_row($sql)) {
	$all.="$orga|";
}

$all.="a|a\n";
$sql=mysql_query("SELECT id FROM i_orga order by orga_name",$conn);
while (list($orga)=mysql_fetch_row($sql)) {
	$all.="$orga|";
}

$all.="a|a\n";
$sql=mysql_query("SELECT location FROM i_locations order by location",$conn);
while (list($loca)=mysql_fetch_row($sql)) {
	$all.="$loca|";
}


$all.="a\n";
$sql=mysql_query("SELECT ort FROM i_locations order by location",$conn);
while (list($loca)=mysql_fetch_row($sql)) {
	$all.="$loca|";
}

$all.="a\n";
$sql=mysql_query("SELECT id FROM i_locations order by location",$conn);
while (list($loca)=mysql_fetch_row($sql)) {
	$all.="$loca|";
}

$all.="a\n";
for ($i=0; $i<count($kategorie);$i++)
{
	$all.="$kategorie[$i]|";
}


mysql_close($conn);

//print $all;
print zippit(xor_encode($all,$key[0]));
?>
