package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.example.demo.service.Impl.PdfService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductoController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private PdfService pdfService;
	
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
	
	@GetMapping("/generar_pdf")
	public ResponseEntity<InputStreamResource> generarPdf(HttpSession session) throws IOException {
		
		List<ProductoEntity> productos = productoService.listarTodo(); 
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		String nombreCompletoUsuario = usuarioEntity.getNombres() + " " + usuarioEntity.getApellidos();
		
				
		Map<String, Object> datosPdf = new HashMap<String, Object>();
		datosPdf.put("productos", productos);
		datosPdf.put("nombreUsuario", nombreCompletoUsuario);
				
		ByteArrayInputStream pdfBytes = pdfService.generarPdfDeHtml("template_pdf", datosPdf);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "inline; filename=productos.pdf");
		
		return ResponseEntity.ok()
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdfBytes));
	}
}
