<?php
/* Schnittstelle DB <-> PTool (Event eintragen)
 * by Enrico Tröger 2004
 * Version 0.1
 *
 * Rückgabe-Werte:
 * -3 Datenbank-Verbindung nicht möglich
 * -2 Fehlerhafte User-Daten
 * -1 alles ok
 * >=0 Event Nr. x ist fehlerhaft
 *
 * ToDo: Daten prüfen
 *
 */


// DB-Verbindung aufbauen
//require("/vpserver/web/vps02042/www/config.php");
require("config.php");
$conn = mysql_connect($host,$dbuser,$dbpwd);
if (!$conn) { die("-3"); }
mysql_select_db($dbname);

// User-Daten prüfen
if (($user=="" OR $pass=="") OR mysql_num_rows(mysql_query("SELECT id FROM m_profil WHERE (user='$user' AND pwd='$pass')",$conn))!=1) { die("-2"); }

$failure = array();
if (count($_POST)) {

	for($i = 0; $i < count($_POST); $i++) {
		$row = explode("|",mysql_escape_string($_POST["a".$i]));
		if (count($row) < 13) {	// Eintrag ungültig, überspringen
			$failure[] = $i;
			continue;
		}
		// Kreis auslesen
		$kreis = "sok";
		$res_kreis = mysql_query("SELECT `kreis` FROM `i_orte` WHERE `name`='".$row[1]."' AND `plz`='".$row[2]."'",$conn);
		if ($res_kreis && mysql_num_rows($res_kreis)) list($kreis) = mysql_fetch_row($res_kreis);
		// Veranstalter auslesen
		$res_orga = mysql_query("SELECT `id` FROM `i_orga` WHERE `orga_name`='".$row[9]."'",$conn);
		if ($res_orga && mysql_num_rows($res_orga)) list($vid) = mysql_fetch_row($res_orga);
		else $vid = $row[9];
		// Location auslesen
		$lid = 0;
		$localt = $row[8];
		$res_loc = mysql_query("SELECT `id` FROM `i_locations` WHERE `location`='".$row[8]."'",$conn);
		if ($res_loc && mysql_num_rows($res_loc)) {
			list($lid) = mysql_fetch_row($res_loc);
			$localt = "";
		}

		$query = "
			INSERT INTO `events` 
			(`id`,`kategorie`,`name`,`date`,`ort`,`plz`,`kreis`,`lid`,`loc_alt`,`vid`,`zeit`,`band`,`text`,`cost`,`user`,`date_add`,`date_modify`,`ok`,`visible`)
			VALUES ('','".$row[7]."','".$row[0]."',".$row[5].$row[4].$row[3].",'".$row[1]."','".$row[2]."',
					'".$kreis."','".$lid."','".$localt."','".$vid."','".$row[10]."','".$row[6]."','".$row[12]."','".$row[11]."','".$user."',NOW(),NOW(),'N','J');
			";
		$res = @mysql_query($query, $conn);
		if (mysql_errno($conn) != 0) $failure[] = $i;
	}
}
mysql_close($conn);
if (count($failure) == 0) $failure[0] = -1;

echo implode("|", $failure);
//echo "0|1";
//echo "-1";
?>