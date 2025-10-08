package org.ifmo.laba3;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class CoordinateData {
    public void saveCoordinate(Coordinate coordinate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            // Начало транзакции
            transaction = session.beginTransaction();

            // Сохранение объекта Coordinate в базу данных
            session.save(coordinate);

            // Подтверждение транзакции
            transaction.commit();

        } catch (Exception e) {
            // Если произошла ошибка, откатываем транзакцию
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();

        } finally {
            // Закрытие сессии
            session.close();
        }
    }

    // Метод для получения всех координат
    public List<Coordinate> getAllCoordinates() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Coordinate> coordinates = null;

        try {
            // Запрос для получения всех координат
            Query<Coordinate> query = session.createQuery("FROM Coordinate", Coordinate.class);
            coordinates = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return coordinates;
    }
}



