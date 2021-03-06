package com.tutorial.hibernate.mappings.association.one2many;
// 3rd
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class One2ManyClient {
	private static SessionFactory factory;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{ 
			factory = new Configuration().configure("hibernate.mysql.cfg.xml").buildSessionFactory(); 
		}catch (Throwable ex) { 
			System.err.println("Failed to create sessionFactory object." + ex); 
			throw new ExceptionInInitializerError(ex); 
		}


		HashSet set1 = new HashSet();
		
		set1.add(new Certificate("SCJP")); 
		set1.add(new Certificate("Oracle")); 
		set1.add(new Certificate("MBA"));

		One2ManyClient ec = new One2ManyClient();

		ec.addEmployee("Jayram", "Rout", 5000, set1);
		listEmployees();
	}
	public Integer addEmployee(String fname, String lname, int salary, Set cert){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		Integer employeeID = null; 
		try{ 
			tx = session.beginTransaction(); 
			Employee employee = new Employee(fname, lname, salary); 
			employee.setCertificates(cert); 
			employeeID = (Integer) session.save(employee); 
			tx.commit(); 
		}catch (HibernateException e) { 
			if (tx!=null) 
				tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
		return employeeID; 
	}

	/* Method to list all the employees detail */ 
	public static void listEmployees( ){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		try{ tx = session.beginTransaction(); 
		List employees = session.createQuery("FROM Employee").list(); 
		for (Iterator iterator1 = employees.iterator();iterator1.hasNext();){ 
			Employee employee = (Employee) iterator1.next(); 
			System.out.print("First Name: " + employee.getFirstName()); 
			System.out.print(" Last Name: " + employee.getLastName()); 
			System.out.println(" Salary: " + employee.getSalary()); 
			Set certificates = employee.getCertificates(); 
			for (Iterator iterator2 = certificates.iterator(); iterator2.hasNext();){ 
				Certificate certName = (Certificate) iterator2.next(); 
				System.out.println("Certificate: " + certName.getName()); 
			} 
		} 
		tx.commit(); 
		}catch (HibernateException e) { 
			if (tx!=null) 
				tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
	}

	/* Method to update salary for an employee */ 
	public void updateEmployee(Integer EmployeeID, int salary ){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		try{ 
			tx = session.beginTransaction(); 
			Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
			employee.setSalary( salary ); 
			session.update(employee); 
			tx.commit(); 
		}catch (HibernateException e) {
			if (tx!=null) 
				tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
	} 
	/* Method to delete an employee from the records */ 
	public void deleteEmployee(Integer EmployeeID){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		try{ 
			tx = session.beginTransaction(); 
			Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
			session.delete(employee); 
			tx.commit(); 
		}catch (HibernateException e) { 
			if (tx!=null) tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
	}
}

