package com.fri.rso.fririders.displaybookings.entities;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "bookings")
@NamedQueries(value = {
        @NamedQuery(name="Booking.findAll", query="SELECT b FROM Booking b"),
        @NamedQuery(name="Booking.findByUserId", query="SELECT b FROM Booking b WHERE b.idUser = :id"),
        @NamedQuery(name="Booking.findByAccommodationId", query="SELECT b FROM Booking b WHERE b.idAccommodation = :id")})
@UuidGenerator(name = "idGenerator")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private int id;

    @Column(name = "id_accommodation")
    private int idAccommodation;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;


    private int people;

    public Booking(){
        this.id = 0;
        this.idAccommodation = 0;
        this.idUser = 0;
        this.fromDate = null;
        this.toDate = null;
        this.people = 0;
    }

    public Booking(int id, int idAccommodation, int idUser, Date fromDate, Date toDate, int people) {
        this.id = id;
        this.idAccommodation = idAccommodation;
        this.idUser = idUser;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.people = people;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAccommodation() {
        return idAccommodation;
    }

    public void setIdAccommodation(int idAcommodation) {
        this.idAccommodation = idAcommodation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
