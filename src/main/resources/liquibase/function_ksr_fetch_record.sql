CREATE OR REPLACE FUNCTION ksr_fetch_record(p_recordable_id IN varchar, p_recordable_type IN varchar)
RETURN number 
IS v_record_id number(38);
BEGIN
    SELECT id INTO v_record_id
    FROM ksr_record 
    WHERE recordable_type = p_recordable_type 
    AND recordable_id = p_recordable_id;
RETURN v_record_id;
EXCEPTION 
    WHEN NO_DATA_FOUND THEN RETURN NULL;
END;