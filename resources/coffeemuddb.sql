CREATE TABLE CMCHAB (
	CMUSERID char (50) NULL ,
	CMABID char (50) NULL ,
	CMABLVL int NULL ,
	CMABAB int NULL ,
	CMABUR int NULL ,
	CMABPF int NULL 
);

ALTER TABLE CMCHAB
	ADD 
	( 
		PRIMARY KEY (CMUSERID,CMABID)
	);

CREATE TABLE CMCHAR (
	CMUSERID char (50) NULL ,
	CMPASS char (50) NULL ,
	CMCLAS char (50) NULL ,
	CMSTRE int NULL ,
	CMRACE char (50) NULL ,
	CMDEXT int NULL ,
	CMCONS int NULL ,
	CMGEND char (50) NULL ,
	CMWISD int NULL ,
	CMINTE int NULL ,
	CMCHAR int NULL ,
	CMHITP int NULL ,
	CMLEVL int NULL ,
	CMMANA int NULL ,
	CMMOVE int NULL ,
	CMDESC char (255) NULL ,
	CMALIG int NULL ,
	CMEXPE int NULL ,
	CMEXLV int NULL ,
	CMWORS char (50) NULL ,
	CMPRAC int NULL ,
	CMTRAI int NULL ,
	CMAGEH int NULL ,
	CMGOLD int NULL ,
	CMWIMP int NULL ,
	CMQUES int NULL ,
	CMROID char (50) NULL ,
	CMDATE datetime NULL ,
	CMCHAN int NULL ,
	CMATTA int NULL ,
	CMAMOR int NULL ,
	CMDAMG int NULL ,
	CMBTMP int NULL ,
	CMLEIG char (50) NULL ,
	CMHEIT int NULL ,
	CMWEIT int NULL 
);

ALTER TABLE CMCHAR
	ADD 
	( 
		PRIMARY KEY (CMUSERID)
	);

CREATE TABLE CMCHFO (
	CMUSERID char (50) NULL ,
	CMFONM int NULL ,
	CMFOID char (50) NULL ,
	CMFOTX text NULL ,
	CMFOLV int NULL ,
	CMFOAB int NULL 
);

ALTER TABLE CMCHFO
	ADD 
	( 
		PRIMARY KEY (CMUSERID,CMFONM)
	);

CREATE TABLE CMCHIT (
	CMUSERID char (50) NULL ,
	CMITNM char (50) NULL ,
	CMITID char (50) NULL ,
	CMITTX text NULL ,
	CMITLO char (50) NULL ,
	CMITWO int NULL ,
	CMITUR int NULL ,
	CMITLV int NULL ,
	CMITAB int NULL ,
	CMHEIT int NULL
);

ALTER TABLE CMCHIT
	ADD 
	( 
		PRIMARY KEY (CMUSERID,CMITNM)
	);

CREATE TABLE CMROCH (
	CMROID char (50) NULL ,
	CMCHNM int NULL ,
	CMCHID char (50) NULL ,
	CMCHTX text NULL ,
	CMCHLV int NULL ,
	CMCHAB int NULL ,
	CMCHRE int NULL 
);

ALTER TABLE CMROCH 
	ADD 
	( 
		PRIMARY KEY (CMROID,CMCHNM)
	);

CREATE TABLE CMROEX (
	CMROID char (50) NULL ,
	CMDIRE int NULL ,
	CMEXID char (50) NULL ,
	CMEXTX text NULL ,
	CMNRID char (50) NULL 
);

ALTER TABLE CMROEX 
	ADD 
	( 
		PRIMARY KEY (CMROID,CMDIRE)
	);

CREATE TABLE CMROIT (
	CMROID char (50) NULL ,
	CMITNM char (50) NULL ,
	CMITID char (50) NULL ,
	CMITLO char (50) NULL ,
	CMITTX text NULL ,
	CMITRE int NULL ,
	CMITUR int NULL ,
	CMITLV int NULL ,
	CMITAB int NULL ,
	CMHEIT int NULL
);

ALTER TABLE CMROIT 
	ADD 
	( 
		PRIMARY KEY (CMROID,CMITNM)
	);

CREATE TABLE CMROOM (
	CMROID char (50) NULL ,
	CMLOID char (50) NULL ,
	CMAREA char (50) NULL ,
	CMDESC1 char (50) NULL ,
	CMDESC2 text NULL ,
	CMROTX text NULL 
);

ALTER TABLE CMROOM 
	ADD 
	( 
		PRIMARY KEY (CMROID)
	);


CREATE TABLE CMAREA (
	CMAREA char (50) NULL ,
	CMTYPE char (50) NULL ,
	CMCLIM int NULL ,
	CMSUBS char (100) NULL ,
	CMDESC text NULL ,
	CMROTX text NULL 
);

ALTER TABLE CMAREA 
	ADD 
	( 
		PRIMARY KEY (CMAREA)
	);

CREATE TABLE CMJRNL (
	CMJKEY char (50) NULL ,
	CMJRNL char (50) NULL ,
	CMFROM char (50) NULL ,
	CMDATE int NULL ,
	CMTONM char (50) NULL ,
	CMSUBJ char (100) NULL ,
	CMMSGT text NULL 
);

ALTER TABLE CMJRNL 
	ADD 
	( 
		PRIMARY KEY (CMJKEY)
	);


