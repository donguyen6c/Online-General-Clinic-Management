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
import java.io.Serializable;
import java.time.LocalTime;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "doctor_working_pattern")
@NamedQueries({
    @NamedQuery(name = "DoctorWorkingPattern.findAll", query = "SELECT d FROM DoctorWorkingPattern d"),
    @NamedQuery(name = "DoctorWorkingPattern.findById", query = "SELECT d FROM DoctorWorkingPattern d WHERE d.id = :id"),
    @NamedQuery(name = "DoctorWorkingPattern.findByDayOfWeek", query = "SELECT d FROM DoctorWorkingPattern d WHERE d.dayOfWeek = :dayOfWeek"),
    @NamedQuery(name = "DoctorWorkingPattern.findByStartTime", query = "SELECT d FROM DoctorWorkingPattern d WHERE d.startTime = :startTime"),
    @NamedQuery(name = "DoctorWorkingPattern.findByEndTime", query = "SELECT d FROM DoctorWorkingPattern d WHERE d.endTime = :endTime")})
public class DoctorWorkingPattern implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "day_of_week")
    private short dayOfWeek;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    private LocalTime startTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_time")
    private LocalTime endTime;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Doctor doctorId;

    public DoctorWorkingPattern() {
    }

    public DoctorWorkingPattern(Integer id) {
        this.id = id;
    }

    public DoctorWorkingPattern(Integer id, short dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(short dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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
        if (!(object instanceof DoctorWorkingPattern)) {
            return false;
        }
        DoctorWorkingPattern other = (DoctorWorkingPattern) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vudo.pojo.DoctorWorkingPattern[ id=" + id + " ]";
    }
    
}
