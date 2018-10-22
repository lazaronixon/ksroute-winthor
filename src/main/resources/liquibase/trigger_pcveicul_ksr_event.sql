CREATE OR REPLACE TRIGGER TRIGGER_PCVEICUL_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF descricao
ON PCVEICUL
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.codveicul, 'Veiculo', 'Save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.codveicul, 'Veiculo', 'Delete', ksr_fetch_record(:old.codveicul, 'Veiculo'), 1, sysdate);
    END IF;
END;
