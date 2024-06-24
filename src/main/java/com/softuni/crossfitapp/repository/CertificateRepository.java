package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CertificateRepository  extends JpaRepository<Certificate, UUID> {
}
