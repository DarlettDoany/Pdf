package org.saeta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="cursos")
public class CursosController {
/*
    @Autowired
    CursosRepository cursosRepository;

    @GetMapping(value="listarcursos")
    public ResponseEntity listarcursos(){
        List<Cursos> listarcursos=cursosRepository.findAll();



        return new ResponseEntity(listarcursos, HttpStatus.OK);
}
    @GetMapping(value="escogercursos/{idcursos}")
    public ResponseEntity escogercursos(@PathVariable("idcursos") String idcursos){
        Optional<Cursos> optionalCursos=cursosRepository.findById(Integer.parseInt(idcursos));
                if (optionalCursos.isPresent()){
                    return new ResponseEntity(optionalCursos.get(), HttpStatus.OK);
                }else{
                    return new ResponseEntity("no existe el usuario", HttpStatus.BAD_REQUEST);
                }

    }

 */
}

