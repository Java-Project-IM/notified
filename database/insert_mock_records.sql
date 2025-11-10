-- Insert mock records for the records table
-- First, we need to get the student_id for our existing students

-- For student 2021-003 (Bob Johnson)
INSERT INTO records (student_id, record_type, created_at) 
SELECT student_id, 'EMAIL_SENT', '2025-11-09 09:15:00'
FROM students WHERE student_number = '2021-003';

INSERT INTO records (student_id, record_type, created_at) 
SELECT student_id, 'EMAIL_SENT', '2025-11-09 14:30:00'
FROM students WHERE student_number = '2021-003';

INSERT INTO records (student_id, record_type, created_at) 
SELECT student_id, 'ENROLLMENT', '2025-11-08 10:00:00'
FROM students WHERE student_number = '2021-003';

-- For student 24-1681 (Juztyne Test)
INSERT INTO records (student_id, record_type, created_at) 
SELECT student_id, 'EMAIL_SENT', '2025-11-10 08:45:00'
FROM students WHERE student_number = '24-1681';

INSERT INTO records (student_id, record_type, created_at) 
SELECT student_id, 'ENROLLMENT', '2025-11-07 11:20:00'
FROM students WHERE student_number = '24-1681';

INSERT INTO records (student_id, record_type, created_at) 
SELECT student_id, 'EMAIL_SENT', '2025-11-06 15:10:00'
FROM students WHERE student_number = '24-1681';
