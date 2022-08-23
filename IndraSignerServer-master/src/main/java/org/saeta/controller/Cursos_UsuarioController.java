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
@RequestMapping(value="cursos_usuario")
public class Cursos_UsuarioController {
    /*
    @Autowired
    Cursos_UsuarioRepository cursos_usuarioRepository;

    @GetMapping(value = "listarcursos_usuario")
    public ResponseEntity listarcursos_usuario() {
        List<Cursos_Usuario> listarcursos_usuario = cursos_usuarioRepository.findAll();


        return new ResponseEntity(listarcursos_usuario, HttpStatus.OK);
    }
    @GetMapping(value="escogercursos_usuario/{idcursos_usuario}")
    public ResponseEntity escogercursos_usuario(@PathVariable("idcursos_usuario")String idcursos_usuario){
        Optional<Cursos_Usuario> optionalCursos_usuario=cursos_usuarioRepository.findById(Integer.parseInt(idcursos_usuario));

        if(optionalCursos_usuario.isPresent()){
            return new ResponseEntity(optionalCursos_usuario.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity("no existe el usuario", HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping(value="obtenercursos_usuario/{idcursos_usuario}")
    public ResponseEntity obtenercursos_usuario(@PathVariable("idcursos_usuario")String idcursos_usuario){
        Optional<Cursos_Usuario> optionalCursos_usuario=cursos_usuarioRepository.obtenercursosalumno(Integer.parseInt(idcursos_usuario));
        if(optionalCursos_usuario.isPresent()) {
            return new ResponseEntity(optionalCursos_usuario.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity("no existe el usuario", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value="generarcursos_usuario/{idcursos_usuario}")
    public ResponseEntity generarcursos_usuario(@PathVariable("idcursos_usuario")String idcursos_usuario){
        Optional<Cursos_Usuario> optionalCursos_usuario=cursos_usuarioRepository.generarcursosalumno(Integer.parseInt(idcursos_usuario));
        if(optionalCursos_usuario.isPresent()){
            return new ResponseEntity(optionalCursos_usuario.get(),HttpStatus.OK);
        }else{
            return new ResponseEntity("no existe el usuario", HttpStatus.BAD_REQUEST);
        }
    }

     */
}
