<?
/* Schnittstelle DB <-> PTool
 * by Enrico Tröger 2003 & 2004
 * inkl. XOR-Verschlüsselung (seit 0.1)
 * und GZip-Komprimierung (seit 0.9)
 * sowie OS-Erkennung (seit 0.9a)
 * dynamische Keys (alle 3 Minuten, seit 0.9d)
 * Version 0.9d
 * Meldungen:
 * 1 Datenbank-Verbindung nicht möglich
 * 2 Fehlerhafte User-Daten
 * 
 * "Netzcodes"
 * nc = 1 => Schlüssel holen
 * nc = 2 => Versionskontrolle
 * nc = 3 => User-Daten holen
 * nc = 4 => News holen
*/

//$user='enrico';
//$pass='F5D1278E8109EDD94E1E4197E04873B9';
//$nc=4;

$version=file("./version");
$key=update_key("./key");


// Komprimierungs-Algorithmus
function zippit($source) {
	return gzencode($source, 9);
	//return $source;
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

// Verschlüsselungs-Algorithmus
function encode($source,$key) {

   $input = $source;

   $td = mcrypt_module_open('des', '', 'cbc', '');
   $iv = mcrypt_create_iv (mcrypt_enc_get_iv_size($td), MCRYPT_ENCRYPT);
   mcrypt_generic_init($td, $key, $iv);
   $encrypted_data = mcrypt_generic($td, $input);
   mcrypt_generic_deinit($td);
   mcrypt_module_close($td);
   return $encrypted_data;
}

function update_key($filename) {

	/*
	Keys 35, 44 und 48 spinnen irgendwie
	*/
	// good keys: 31,40
	// News + Daten müssen innerhalb von 3 Minuten geholt werden, ansonten wird neuer Key generiert
	$mtime = time() - 180;
	if (file_exists($filename) && filemtime($filename) > $mtime) {
		$key = file($filename);
		$key = $key[0];
	}
	else {
		srand ((double)microtime()*1000000);
		//$key = rand(30,60);
		$key=31;	// mal wieder ändern!!!
		$fd = fopen($filename, "w");
		fwrite($fd, $key);
		fclose($fd);
	}
	return $key;
}


// Versionskontrolle
if ($nc==2) {
	die(xor_encode($version[0],17));
	//die($version[0]);
}

// DB-Verbindung aufbauen
//require("/vpserver/web/vps02042/www/config.php");
require("config.php");
$conn = @mysql_connect($host,$dbuser,$dbpwd);
if (!$conn) { die(xor_encode("1",17)); }
mysql_select_db($dbname);

// User-Daten prüfen
if (($user=="" OR $pass=="") OR mysql_num_rows(mysql_query("SELECT id FROM m_profil WHERE (user='$user' AND pwd='$pass')",$conn))!=1) { die(xor_encode("2",17)); }

// Schlüssel mit festem Schlüssel >17< codieren und ausgeben
if ($nc==1) {
	print xor_encode($key."null",17);
	//print $key."null";	// just Debug *g*
	exit;
}

// News holen
if ($nc==4) {
	$sql=mysql_query("SELECT DATE_FORMAT(datum,'%d.%m.%Y'),betreff,text FROM news ORDER by datum DESC LIMIT 0,5",$conn);
	while (list($datum,$betreff,$text)=mysql_fetch_row($sql)) {
		$news.=$datum."|".$betreff."|".$text."|";
	}
	print zippit(xor_encode($news,$key));
	//print zippit($news);
	//print $news;	// just Debug *g*
	exit;
}

// User-Daten holen
if ($nc==3) {
	list($tel,$email,$hp)=mysql_fetch_row(mysql_query("SELECT telefon,email,homepage FROM m_profil WHERE (user='$user' AND pwd='$pass')",$conn));
	print xor_encode($email."|".$hp."|".$tel,0);
	//print $email."|".$hp."|".$tel;
	exit;
}


// Daten holen

// Stats eintragen
$ua = getenv("HTTP_USER_AGENT");
$daten = explode (" ", $ua);
$daten2 = explode ("/", $daten[1]);
$sql=@mysql_query("INSERT INTO psagent VALUES ('',NOW(),'$daten[2]','$daten2[1]')",$conn);

// SQL-Abfrage Bedingungen für den/die Kreise festlegen
//if ($area=="all" or $area=="") {
	$bed_kreis="1";
//	$area="all";
//}
//else { $bed_kreis="kreis='$area'"; }

// DIE SQL-Abfrage (gilt für "Alle Kreise" und die jeweiligen Kreise)
$date_ok=date("Y-m-d",mktime(0,0,0,date("m"),date("d")+14,date("Y")));
$sql=mysql_query("SELECT id,kategorie,name,DATE_FORMAT(date,'%d.%m.%Y'),ort,plz,kreis,lid,loc_alt,vid,zeit,band,text,cost,flyerlnk,DATE_FORMAT(date_modify,'%d.%m.%Y %H:%i') FROM events WHERE (ok='J') AND (visible='J') AND (date < '$date_ok' OR user='$user') order by date",$conn);

$all="0".strlen(mysql_num_rows($sql)).mysql_num_rows($sql);
$all.="\n".date("d.m.Y - H:i");
while (list($id,$kategorie,$name,$datum,$ort,$plz,$kreis,$lid,$loc_alt,$vid,$zeit,$band,$text,$cost,$flyerlnk,$datum_modify)=mysql_fetch_row($sql)) {
	// Location-Details aus i_locations ziehen
	list($loca)=mysql_fetch_row(mysql_query("SELECT location FROM i_locations WHERE id=$lid",$conn));
	if ($loca=="") { $loca=$loc_alt; }
	// Kategorie prüfen
	if ($kategorie=="") { $kategorie=" - "; }
	// Ort und Location zusammen setzen, wenn Location vorhanden
	if ($loca!="") { $ort_loca="$ort / $loca"; }
	else { $ort_loca=$ort; }

	// Den ganzen Schrutz ausgeben
	$all.="\n$id|$name|$ort_loca|$kategorie|$band|$datum|$plz|$kreis|$vid|$zeit|$text|$cost|$flyerlnk|$datum_modify";
}

mysql_close($conn);

print zippit(xor_encode($all,$key));
//print zippit($all);
//print $all;

?>
