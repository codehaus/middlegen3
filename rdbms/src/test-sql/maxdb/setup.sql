create table PP (
   a int not null,
   primary key (a)
) type=INNODB;
create table QQ (
   m int not null,
   primary key (m),
   foreign key (m) references PP(a)
) type=INNODB;

create table TT (
   a int not null,
   primary key (a)
) type=INNODB;
create table UU (
   m int not null,
   n int not null,
   primary key (m,n),
   index m_ind(m),
   index n_ind(n),
   foreign key (m) references TT(a),
   foreign key (n) references TT(a)
) type=INNODB;

create table VV (
   a int not null,
   b int not null,
   primary key (a,b)
) type=INNODB;
create table WW (
   m int not null,
   n int not null,
   primary key (m,n),
   foreign key (m,n) references VV(a,b)
) type=INNODB;

create table XX (
   a int not null,
   b int not null,
   c int not null,
   d int not null,
   primary key (a,b)
) type=INNODB;
create table YY (
   m int not null,
   n int not null,
   o int not null,
   p int not null,
   primary key (m,n),
   index mn_ind(m,n),
   index op_ind(o,p),
   foreign key (m,n) references XX(a,b),
   foreign key (o,p) references XX(a,b)
) type=INNODB;

create table ZZ (
   a int not null,
   b varchar(32) not null,
   c int not null,
   d int not null,
   primary key (a,b)
) type=INNODB;
