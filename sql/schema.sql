create table MatavimoVienetas (
        Id bigint not null,
        kodas varchar(20) not null,
        pavadinimas varchar(100) not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (kodas)
    );

--drop table prekes
create table Prekes (
        Id bigint not null,
        pavadinimas varchar(100) not null,
        matVienetasId bigint not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (pavadinimas),
        FOREIGN KEY (matVienetasId) REFERENCES MatavimoVienetas(Id)
    );

alter table prekes add column islaukaMesai bigint null;
alter table prekes add column islaukaPienui bigint null;

--drop view vPrekes
create view vPrekes as 
select p.Id, p.Pavadinimas, p.ISLAUKAMESAI, p.ISLAUKAPIENUI, v.kodas as matVienetas from prekes p left join MATAVIMOVIENETAS v on p.MATVIENETASID = v.id;

--drop table prekes
create table Tiekejai (
        Id bigint not null,
        pavadinimas varchar(100) not null,
        telefonas varchar(50) not null,
        adresas varchar(200) not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (pavadinimas)
    );

    --drop table prekes
create table Imones (
        Id bigint not null,
        pavadinimas varchar(100) not null,
        telefonas varchar(50) not null,
        adresas varchar(200) not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (pavadinimas)
    );
	
	-- drop table saskaitos
    create table Saskaitos (
        Id bigint not null,
        arSaskaita bit not null,
        numeris varchar(100) not null,
        imoneId bigint not null,
        tiekejasId bigint null,
        data timestamp not null,
        sumaSuPvm numeric not null,
        sumaBePvm numeric not null,
        sumaPvm numeric not null,
        prekiuKiekis integer not null,
        statusas integer,
        primary key (Id),
        unique (numeris),
        FOREIGN KEY (imoneId) REFERENCES Imones(Id),
        FOREIGN KEY (tiekejasId) REFERENCES Tiekejai(Id)
    );
-- drop table SaskaitosPrekes
    create table SaskaitosPrekes (
        Id bigint not null,
        serija varchar(100) not null,
        saskaitaId bigint not null,
        prekeId bigint not null,
        matavimoVienetasId bigint not null,
        kiekis numeric not null,
        galiojaIki timestamp,
        kainaBePvm numeric,
        nuolaidosProc numeric,
        pvm numeric,
        sumaPvm numeric,
        sumaSuPvm numeric,
        sumaBePvm numeric,
        primary key (Id),
        FOREIGN KEY (matavimoVienetasId) REFERENCES MatavimoVienetas(Id),
        FOREIGN KEY (prekeId) REFERENCES Prekes(Id),
        FOREIGN KEY (saskaitaId) REFERENCES SASKAITOS(ID)
    );
-- drop view vSaskaitos
create view vSaskaitos as 
select s.Id, s.arSaskaita, s.numeris, s.SUMASUPVM, s.STATUSAS, s.DATA, i.PAVADINIMAS as imone, t.PAVADINIMAS as tiekejas
	from saskaitos s 	left join imones i on s.IMONEID = i.id
					left join TIEKEJAI t on s.TIEKEJASID = t.id;



create table Nurasymai (
        Id bigint not null,
        numeris varchar(100) not null,
        imoneId bigint not null,
        data timestamp not null,
        prekiuKiekis integer not null,
        statusas integer,
        primary key (Id),
        FOREIGN KEY (imoneId) REFERENCES Imones(Id)
    );

    create table NurasymoPrekes (
        Id bigint not null,
        serija varchar(100) not null,
        nurasymasId bigint not null,
        prekeId bigint not null,
        matavimoVienetasId bigint not null,
        kiekis numeric not null,
        primary key (Id),
        FOREIGN KEY (matavimoVienetasId) REFERENCES MatavimoVienetas(Id),
        FOREIGN KEY (prekeId) REFERENCES Prekes(Id)
    );

 -- drop view vNurasymai
create view vNurasymai as 
select n.Id, n.numeris, n.STATUSAS, n.DATA, i.PAVADINIMAS as imone
	from NURASYMAI n 	left join imones i on n.IMONEID = i.id; 

	     --drop table GyvunuRusys
create table GyvunuRusys (
        Id bigint not null,
        pavadinimas varchar(100) not null,
        primary key (Id),
        unique (pavadinimas)
    );

	
	create table GydomuGyvunuZurnalas (
        Id bigint not null,
        eilesNumeris bigint,
        imoneId bigint not null,
        laikytojas varchar(200) not null,
        registracijosData timestamp not null,
        pirmuPozymiuData timestamp,
        diagnoze varchar(500),
        gyvunuSarasas varchar(500),
        gydymas varchar(500),
        primary key (Id),
		FOREIGN KEY (imoneId) REFERENCES IMONES(Id)
    );
	
	create table ZurnaloGyvunai (
        Id bigint not null,
        zurnaloId bigint not null,
        gyvunoRusisId bigint not null,
        numeris varchar(400),
        amzius varchar(100),
        primary key (Id),
		FOREIGN KEY (zurnaloId) REFERENCES GydomuGyvunuZurnalas(Id)
    );

    create table ZurnaloVaistai (
        Id bigint not null,
        zurnaloId bigint not null,
        prekeId bigint not null,
        receptas varchar(400),
        matavimoVienetasId bigint not null,
        kiekis numeric not null,
        primary key (Id),
		FOREIGN KEY (zurnaloId) REFERENCES GydomuGyvunuZurnalas(Id),
		FOREIGN KEY (prekeId) REFERENCES Prekes(Id),
		FOREIGN KEY (matavimoVienetasId) REFERENCES MatavimoVienetas(Id)
    );
	
create view vGydomuGyvunuZurnalas as 
select n.Id, n.eilesNumeris, n.laikytojas, n.registracijosData, n.gyvunuSarasas, n.gydymas, i.PAVADINIMAS as imone
	from GydomuGyvunuZurnalas n left join imones i on n.IMONEID = i.id; 

	-- drop view vGyvunai
-- select * from vGyvunai
create view vGyvunai as 
select l.Id, l.numeris, l.GYVUNORUSISID, i.LAIKYTOJAS
	from ZurnaloGyvunai l left join GydomuGyvunuZurnalas i on l.ZURNALOID = i.id;

--drop table likuciai
create table Likuciai (
        Id bigint not null,
        dokumentas varchar(100) not null,
        irasoId bigint,
        prekeId bigint not null,
        imoneId bigint not null,
        matavimoVienetasId bigint not null,
        kiekis numeric not null,
        galiojaIki timestamp,
        serija varchar(100),
        data timestamp not null,
        arSaskaita bit not null,
        zurnaloId bigint null,
        zurnaloVaistoId bigint null,
        saskaitosId bigint null,
        saskaitosPrekesId bigint null,
        pirminisId bigint null,
        primary key (Id),
	   FOREIGN KEY (matavimoVienetasId) REFERENCES MatavimoVienetas(Id),
        FOREIGN KEY (prekeId) REFERENCES Prekes(Id) ,
        FOREIGN KEY (imoneId) REFERENCES Imones(Id) ,
        FOREIGN KEY (zurnaloId) REFERENCES GydomuGyvunuZurnalas(Id) ,
        FOREIGN KEY (zurnaloVaistoId) REFERENCES ZurnaloVaistai(Id) ,
        FOREIGN KEY (saskaitosId) REFERENCES SASKAITOS (ID),
        FOREIGN KEY (saskaitosPrekesId) REFERENCES SASKAITOSPREKES(ID),
        FOREIGN KEY (pirminisId) REFERENCES Likuciai(ID)
    );



    -- drop view vLikuciaiInt
    -- select * from vLikuciaiInt
create view vLikuciaiInt as 
select l.PREKEID, min(l.MATAVIMOVIENETASID) as MATAVIMOVIENETASID, l.IMONEID, sum(l.KIEKIS) as kiekis, max(l.DATA) as data, min(l.id) as id, sum(Case When l.KIEKIS > 0 Then l.KIEKIS Else 0 End) as pajamuota
	from likuciai l group by l.prekeid, l.imoneid;

  -- drop view vLikuciai
    -- select * from vLikuciai
create view vLikuciai as 
select l.Id, l.kiekis, l.PAJAMUOTA, l.DATA, i.PAVADINIMAS as imone, mv.KODAS as matavimovienetas, p.PAVADINIMAS as preke
	from vLikuciaiInt l left join imones i on l.IMONEID = i.id
					left join Prekes p on l.PREKEID = p.id
					left join MATAVIMOVIENETAS mv on mv.id = l.MATAVIMOVIENETASID;

-- v2
alter table prekes add column islaukaMesai bigint null;
alter table prekes add column islaukaPienui bigint null;

drop view vPrekes;
create view vPrekes as 
select p.Id, p.Pavadinimas, p.ISLAUKAMESAI, p.ISLAUKAPIENUI, v.kodas as matVienetas from prekes p left join MATAVIMOVIENETAS v on p.MATVIENETASID = v.id;

alter table GydomuGyvunuZurnalas add column islaukaMesai timestamp null;
alter table GydomuGyvunuZurnalas add column islaukaPienui timestamp null;

drop view vGydomuGyvunuZurnalas;
create view vGydomuGyvunuZurnalas as 
select n.Id, n.eilesNumeris, n.laikytojas, n.registracijosData, n.gyvunuSarasas, n.gydymas, n.islaukaMesai, n.islaukaPienui, i.PAVADINIMAS as imone
	from GydomuGyvunuZurnalas n left join imones i on n.IMONEID = i.id; 

alter table likuciai alter column DOKUMENTAS SET null;
alter table likuciai add column nurasymoId bigint null;
alter table likuciai add CONSTRAINT likuciai_nurasymoId FOREIGN KEY (nurasymoId) REFERENCES nurasymai(Id);

--drop table nustatymai
    create table Nustatymai (
        Id bigint not null,
        kodas varchar(100) not null,
        reiksme varchar(500),
        primary key (Id),
        unique (kodas)
    );

    delete from nustatymai;
    insert into nustatymai (id, kodas, reiksme) values (1, 'KIEKIS=1_STR', 'D.S. ');
    insert into nustatymai (id, kodas, reiksme) values (2, 'KIEKIS>1_STR', 'D.t.d. No {kiekis} {mvienetas}{newline}');

 alter table SaskaitosPrekes alter column kiekis numeric(15,3);
 alter table SaskaitosPrekes alter column kainaBePvm numeric(15,3);
 alter table SaskaitosPrekes alter column nuolaidosProc numeric(15,3);
 alter table SaskaitosPrekes alter column pvm numeric(15,3);
 alter table SaskaitosPrekes alter column sumaPvm numeric(15,3);
 alter table SaskaitosPrekes alter column sumaSuPvm numeric(15,3);
 alter table SaskaitosPrekes alter column sumaBePvm numeric(15,3);

drop view vSaskaitos;
 alter table Saskaitos alter column sumaSuPvm numeric(15,3);
 alter table Saskaitos alter column sumaBePvm numeric(15,3);
 alter table Saskaitos alter column sumaPvm numeric(15,3);
 
create view vSaskaitos as 
select s.Id, s.arSaskaita, s.numeris, s.SUMASUPVM, s.STATUSAS, s.DATA, i.PAVADINIMAS as imone, t.PAVADINIMAS as tiekejas
	from saskaitos s 	left join imones i on s.IMONEID = i.id
					left join TIEKEJAI t on s.TIEKEJASID = t.id;

alter table ZurnaloVaistai alter column kiekis numeric(15,3);

drop view vLikuciai;
drop view vLikuciaiInt;

 
 alter table Likuciai alter column kiekis numeric(15,3);

create view vLikuciaiInt as 
select l.PREKEID, min(l.MATAVIMOVIENETASID) as MATAVIMOVIENETASID, l.IMONEID, sum(l.KIEKIS) as kiekis, max(l.DATA) as data, min(l.id) as id, sum(Case When l.KIEKIS > 0 Then l.KIEKIS Else 0 End) as pajamuota
	from likuciai l group by l.prekeid, l.imoneid;

create view vLikuciai as 
select l.Id, l.kiekis, l.PAJAMUOTA, l.DATA, i.PAVADINIMAS as imone, mv.KODAS as matavimovienetas, p.PAVADINIMAS as preke
	from vLikuciaiInt l left join imones i on l.IMONEID = i.id
					left join Prekes p on l.PREKEID = p.id
					left join MATAVIMOVIENETAS mv on mv.id = l.MATAVIMOVIENETASID;

		