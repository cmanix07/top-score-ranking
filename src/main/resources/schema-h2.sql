DROP TABLE IF EXISTS PLAYER;
CREATE TABLE PLAYER
(
    playerId    NUMBER(5)    NOT NULL PRIMARY KEY,
    playerName  VARCHAR(50)  NOT NULL,
    playerScore NUMBER(10),
    scoreTime   TIMESTAMP default to_timestamp(to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') NOT NULL
);