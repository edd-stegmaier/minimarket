package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolService {

	@Autowired
	private RolRepository rolRepository;

	public Optional<Rol> findByNombre(String nombre) {
		return rolRepository.findByNombre(nombre);
	}
}