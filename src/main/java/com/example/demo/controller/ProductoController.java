package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.ProductoEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductoController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping("/menu")
	public String showMenu(HttpSession session, Model model) {
		
	    if (session.getAttribute("usuario") == null) {
	    	return "redirect:/";
	    }
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombreUsuario", usuarioEntity.getNombres() + " " + usuarioEntity.getApellidos());
		
		// Listar productos
		List<ProductoEntity> productos = productoService.listarTodo();
		model.addAttribute("productos", productos);
		
		return "menu";
	}
	
	@GetMapping("/agregar_producto")
	public String showAgregarProducto(HttpSession session, Model model) {
		if (session.getAttribute("usuario") == null) {
	    	return "redirect:/";
	    }
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombreUsuario", usuarioEntity.getNombres() + " " + usuarioEntity.getApellidos());
		
		model.addAttribute("producto", new ProductoEntity());
		model.addAttribute("categorias", categoriaRepository.findAll());
		return "agregar_producto";
	}
	
	@PostMapping("/agregar_producto")
	public String agregarProducto(@DateTimeFormat(pattern = "yyyy-mm-dd") ProductoEntity productoEntity) {
		productoService.crearProducto(productoEntity);
		return "redirect:/menu";
	}
	
	@GetMapping("/editar_producto/{id}")
	public String showEditarProducto(HttpSession session, @DateTimeFormat(pattern = "yyyy-mm-dd") @PathVariable("id") Long id, Model model) {
		if (session.getAttribute("usuario") == null) {
	    	return "redirect:/";
	    }
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombreUsuario", usuarioEntity.getNombres() + " " + usuarioEntity.getApellidos());
		
		
		ProductoEntity productoBuscar = productoService.buscarPorId(id);
		
		model.addAttribute("categorias", categoriaRepository.findAll());
		model.addAttribute("producto", productoBuscar);
		
		return "editar_producto";
	}
	
	@PostMapping("/editar_producto")
	public String editarProducto(Model model, ProductoEntity productoEntity) {
		
		productoService.actualizarProducto(productoEntity);
		return "redirect:/menu";
	}
	
	@GetMapping("/buscar/{id}")
	public String buscarPorId(HttpSession session, @PathVariable("id") Long id, Model model) {
		if (session.getAttribute("usuario") == null) {
	    	return "redirect:/";
	    }
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("foto", usuarioEntity.getUrlImagen());
		model.addAttribute("nombreUsuario", usuarioEntity.getNombres() + " " + usuarioEntity.getApellidos());
		
		ProductoEntity productoEncontrado = productoService.buscarPorId(id);
		model.addAttribute("producto", productoEncontrado);
		return "buscar";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarProducto( @PathVariable("id") Long id) {
		productoService.eliminarProducto(id);
		return "redirect:/menu";
	}
}
