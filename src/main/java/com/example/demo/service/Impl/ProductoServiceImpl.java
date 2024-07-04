package com.example.demo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ProductoEntity;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService{
	@Autowired
	private ProductoRepository productoRepository;
	
	@Override
	public List<ProductoEntity> listarTodo() {
		return productoRepository.findAll();
	}

	@Override
	public ProductoEntity buscarPorId(Long id) {
		return productoRepository.findById(id.longValue()).get();
	}

	@Override
	public ProductoEntity crearProducto(ProductoEntity productoEntity) {
		return productoRepository.save(productoEntity);
	}

	@Override
	public ProductoEntity actualizarProducto(ProductoEntity productoEntity) {
		ProductoEntity productoBuscado = buscarPorId(productoEntity.getProductoId());
		if(productoBuscado != null) { 
			productoBuscado.setNombre(productoEntity.getNombre());
			productoBuscado.setStock(productoEntity.getStock());
			productoBuscado.setPrecio(productoEntity.getPrecio());
			productoBuscado.setCategoria(productoEntity.getCategoria());
			return productoRepository.save(productoBuscado);
		}
		return null;
	}

	@Override
	public void eliminarProducto(Long id) {
		productoRepository.deleteById(id);
		
	}

}
