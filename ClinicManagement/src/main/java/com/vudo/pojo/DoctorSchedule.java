/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "doctor_schedule")
@NamedQueries({
    @NamedQuery(name = "DoctorSchedule.findAll", query = "SELECT d FROM DoctorSchedule d"),
    @NamedQuery(name = "DoctorSchedule.findById", query = "SELECT d FROM DoctorSchedule d WHERE d.id = :id"),
    @NamedQuery(name = "DoctorSchedule.findByWorkDate", query = "SELECT d FROM DoctorSchedule d WHERE d.workDate = :workDate"),
    @NamedQuery(name = "DoctorSchedule.findByStartTime", query = "SELECT d FROM DoctorSchedule d WHERE d.startTime = :startTime"),
    @NamedQuery(name = "DoctorSchedule.findByEndTime", query = "SELECT d FROM DoctorSchedule d WHERE d.endTime = :endTime"),
    @NamedQuery(name = "DoctorSchedule.findByNote", query = "SELECT d FROM DoctorSchedule d WHERE d.note = :note"),
    @NamedQuery(name = "DoctorSchedule.findByIsAvailable", query = "SELECT d FROM DoctorSchedule d WHERE d.isAvailable = :isAvailable")})
public class DoctorSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "work_date")
    private LocalDate workDate;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    @Size(max = 255)
    @Column(name = "note")
    private String note;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Doctor doctorId;

    public DoctorSchedule() {
    }

    public DoctorSchedule(Integer id) {
        this.id = id;
    }

    public DoctorSchedule(Integer id, LocalDate workDate) {
        this.id = id;
        this.workDate = workDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DoctorSchedule)) {
            return false;
        }
        DoctorSchedule other = (DoctorSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vudo.pojo.DoctorSchedule[ id=" + id + " ]";
    }
    
}
