// THIS FILE IS GENERATED - DO NOT EDIT
//
// Copyright
//
// Copyright (c) phloc systems 2004 - 2012
// http://www.phloc.com
//
// All Rights Reserved
// Use, duplication or disclosure restricted by phloc systems
//
// Vienna, 2004 - 2012
// This file was generated by com.phloc.webui.pui.calendar.MainCreateCalendarTranslations2

// full day names, starting and ending with Sunday!
Calendar._DN = new Array
("Nedele",
 "Pondeli",
 "Utery",
 "Streda",
 "Ctvrtek",
 "Patek",
 "Sobota",
 "Nedele");

//Please note that the following array of short day names (and the same goes
//for short month names, _SMN) isn't absolutely necessary.  We give it here
//for exemplification on how one can customize the short day names, but if
//they are simply the first N letters of the full name you can simply say:
//
//Calendar._SDN_len = N; // short day name length
//Calendar._SMN_len = N; // short month name length
//
//If N = 3 then this is not needed either since we assume a value of 3 if not
//present, to be compatible with translation files that were written before
//this feature.

// short day names, starting and ending with Sunday
Calendar._SDN = new Array
("Ne",
 "Po",
 "Ut",
 "St",
 "Ct",
 "Pa",
 "So",
 "Ne");

// First day of the week. "0" means display Sunday first, "1" means display
// Monday first, etc.
Calendar._FD = 1;

// full month names, from January to December
Calendar._MN = new Array
("Leden",
 "Unor",
 "Brezen",
 "Duben",
 "Kveten",
 "Cerven",
 "Cervenec",
 "Srpen",
 "Zari",
 "Rijen",
 "Listopad",
 "Prosinec");

// short month names, from January to December
Calendar._SMN = new Array
("Led",
 "Uno",
 "Bre",
 "Dub",
 "Kve",
 "Cvn",
 "Cvc",
 "Srp",
 "Zar",
 "Rij",
 "Lis",
 "Pro");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "O kalendari";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this ;-)
"For latest version visit: http://www.dynarch.com/projects/calendar/\n" +
"Distributed under GNU LGPL.  See http://gnu.org/licenses/lgpl.html for details." +
"\n\n" + "Vybrat datum:\n- Pouzit «, » buttons to select year\n- Pouzit ‹, › buttons to select month\n- Podrzte tlacitko mysi na nektery z vyse uvedenych tlacitek pro rychlejsi vyber.";
Calendar._TT["ABOUT_TIME"] = "\n\n" + "Vybrat cas:\n- Kliknutim na udaj jej zvysite\n- nebo Shift kliknutim snizite\n- nebo kliknutim a tazenim muzete snaze vybirat.";

Calendar._TT["PREV_YEAR"] = "Pred. rok (pridrzte menu)";
Calendar._TT["PREV_MONTH"] = "Pred. mesic (pridrzte menu)";
Calendar._TT["GO_TODAY"] = "Jit na dnes";
Calendar._TT["NEXT_MONTH"] = "Dalsi mesic (pridrzte menu)";
Calendar._TT["NEXT_YEAR"] = "Dalsi rok (pridrzte menu)";
Calendar._TT["SEL_DATE"] = "Vyber datum";
Calendar._TT["DRAG_TO_MOVE"] = "Tazenim presunout";
Calendar._TT["PART_TODAY"] = " (Dnes)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Tyden zacina %s";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Zavrit";
Calendar._TT["TODAY"] = "Dnes";
Calendar._TT["TIME_PART"] = "(Shift-)Kliknete nebo posouvejte pro zmenu hodnoty";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d.%m.%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%A, %e. %B";

// phloc added
Calendar._TT["DEF_TIME_FORMAT"] = "%H:%M:%S";

Calendar._TT["WK"] = "wk";
Calendar._TT["TIME"] = "Cas:";
