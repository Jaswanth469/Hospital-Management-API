package com.java.builder;


import com.java.dto.AppointmentDTO;
import com.java.model.Appointment;
import com.java.model.Doctor;
import com.java.model.Patient;

public class AppointmentBuilder {

    public static Appointment buildAppointmentFromDTO(AppointmentDTO dto, Doctor doctor,Patient patient, String status) {
        if (dto == null || doctor == null) {
            return null;
        }

       return Appointment.builder()
        	.doctor(doctor)
        	.patient(patient)
        	.appointmentDate(dto.getAppointmentDate())
        	.startTime(dto.getStartTime())
        	.endTime(dto.getEndTime())
        	.status(status)
        	.notes(dto.getNotes())
        	.build();
       
    }

    public static AppointmentDTO buildDTOFromAppointment(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        return AppointmentDTO.builder()
        		.appointmentId(appointment.getAppointmentId())
        		.doctorId(appointment.getDoctor().getStaffId())
        		.patientId(appointment.getPatient().getPatientId())
        		.appointmentDate(appointment.getAppointmentDate())
        		.startTime(appointment.getStartTime())
        		.endTime(appointment.getEndTime())
        		.notes(appointment.getNotes())
        		.build();
    }
}
