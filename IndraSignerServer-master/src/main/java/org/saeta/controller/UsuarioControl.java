package org.saeta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="usuario")
public class UsuarioControl {
/*
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value="listarusuario")
    public ResponseEntity listarusuario(){

        List<Usuario> listausuario=usuarioRepository.findAll();


        return new ResponseEntity(listausuario, HttpStatus.OK);
    }

*/
}
