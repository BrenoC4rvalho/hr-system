-- V5: Add manager id to departments

ALTER TABLE departments 
ADD COLUMN manager_id BIGINT; 

ALTER TABLE departments
ADD CONSTRAINT fk_departments_manager_id_fkey
FOREIGN KEY (manager_id) REFERENCES employees(id);

