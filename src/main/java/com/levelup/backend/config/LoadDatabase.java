package com.levelup.backend.config;

import com.levelup.backend.models.*;
import com.levelup.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class LoadDatabase {
    

    private final Faker faker = new Faker();
    
    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            BlogRepository blogRepository,
            ContactoRepository contactoRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            log.info("Verificando datos en la base de datos...");

            // Verificar si ya existen datos
            long usuariosCount = usuarioRepository.count();
            long categoriasCount = categoriaRepository.count();
            long productosCount = productoRepository.count();

            if (usuariosCount > 0 || categoriasCount > 0 || productosCount > 0) {
                log.info("========================================");
                log.info("⚠️  Datos ya existen en la base de datos");
                log.info("========================================");
                log.info("Usuarios: {}", usuariosCount);
                log.info("Categorías: {}", categoriasCount);
                log.info("Productos: {}", productosCount);
                log.info("Blogs: {}", blogRepository.count());
                log.info("Contactos: {}", contactoRepository.count());
                log.info("========================================");
                log.info("💡 Si deseas recargar los datos, cambia ddl-auto a 'create-drop'");
                log.info("========================================");
                return;
            }

            log.info("No hay datos, iniciando carga de datos de prueba...");

            // Crear usuarios
            List<Usuario> usuarios = crearUsuarios(usuarioRepository, passwordEncoder);
            log.info("✓ {} usuarios creados", usuarios.size());
            
            // Crear categorías
            List<Categoria> categorias = crearCategorias(categoriaRepository);
            log.info("✓ {} categorías creadas", categorias.size());
            
            // Crear productos
            List<Producto> productos = crearProductos(productoRepository, categorias);
            log.info("✓ {} productos creados", productos.size());
            
            // Crear blogs
            List<Blog> blogs = crearBlogs(blogRepository);
            log.info("✓ {} blogs creados", blogs.size());
            
            // Crear contactos
            List<Contacto> contactos = crearContactos(contactoRepository);
            log.info("✓ {} mensajes de contacto creados", contactos.size());
            
            log.info("========================================");
            log.info("✅ Datos de prueba cargados exitosamente!");
            log.info("========================================");
            log.info("Usuario Admin - Email: admin@test.com | Password: admin123");
            log.info("Usuario Test  - Email: user@test.com  | Password: user123");
            log.info("Swagger UI: http://localhost:8080/swagger-ui.html");
            log.info("========================================");
        };
    }
    
    private List<Usuario> crearUsuarios(UsuarioRepository repository,PasswordEncoder passwordEncoder) {
        List<Usuario> usuarios = new ArrayList<>();
        

        Usuario admin = new Usuario();
        admin.setNombre("Admin Usuario");
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setTelefono("555-0001");
        admin.setDireccion("Calle Principal 123");
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("ADMIN");
        adminRoles.add("USER");
        admin.setRoles(adminRoles);
        admin.setActivo(true);
        usuarios.add(repository.save(admin));
        

        Usuario user = new Usuario();
        user.setNombre("Test Usuario");
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setTelefono("555-0002");
        user.setDireccion("Avenida Secundaria 456");
        Set<String> userRoles = new HashSet<>();
        userRoles.add("USER");
        user.setRoles(userRoles);
        user.setActivo(true);
        usuarios.add(repository.save(user));
        

        for (int i = 0; i < 5; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombre(faker.name().fullName());
            usuario.setEmail(faker.internet().emailAddress());
            usuario.setPassword(passwordEncoder.encode("password123"));//<-- Contraseña por defecto Encriptada
            usuario.setTelefono(faker.phoneNumber().cellPhone());
            usuario.setDireccion(faker.address().fullAddress());
            Set<String> roles = new HashSet<>();
            roles.add("USER");
            usuario.setRoles(roles);
            usuario.setActivo(true);
            usuarios.add(repository.save(usuario));
        }
        
        return usuarios;
    }
    
    private List<Categoria> crearCategorias(CategoriaRepository repository) {
        List<Categoria> categorias = new ArrayList<>();
        String[] nombres = {"Consolas", "Videojuegos", "Accesorios", "PC Gaming", "Periféricos", "Coleccionables"};
        
        for (String nombre : nombres) {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion("Categoría de " + nombre.toLowerCase());
            categoria.setImagen("/images/categorias/" + nombre.toLowerCase() + ".jpg");
            categoria.setActivo(true);
            categorias.add(repository.save(categoria));
        }
        
        return categorias;
    }
    
    private List<Producto> crearProductos(ProductoRepository repository, List<Categoria> categorias) {
        List<Producto> productos = new ArrayList<>();
        

        String[][] productosData = {
                {"Catan", "Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan.", "29990", "Juegos de Mesa", "Catan Studio", "https://bucket-levelup.s3.us-east-1.amazonaws.com/catan.png"},
                {"Carcassonne", "Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne.", "24990", "Juegos de Mesa", "Z-Man Games", "https://bucket-levelup.s3.us-east-1.amazonaws.com/carcassonne.png"},
                {"Controlador Inalámbrico Xbox Series X", "Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC.", "59990", "Accesorios", "Microsoft", "https://bucket-levelup.s3.us-east-1.amazonaws.com/xbox_controller.png"},
                {"Auriculares Gamer HyperX Cloud II", "Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad", "79990", "Accesorios", "HyperX", "https://bucket-levelup.s3.us-east-1.amazonaws.com/hyperx_cloud.png"},
                {"PlayStation 5", "La consola de última generación de Sony, que ofrece gráficos impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva.", "549990", "Consolas", "Sony", "https://bucket-levelup.s3.us-east-1.amazonaws.com/ps5-test.png"},
                {"PC Gamer ASUS ROG Strix", "Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes para ofrecer un rendimiento excepcional", "1299990", "Computadores Gamers", "ASUS", "https://bucket-levelup.s3.us-east-1.amazonaws.com/pc gamer.png"},
                {"Silla Gamer Secretlab Titan", "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable para sesiones de juego prolongadas.", "349990", "Sillas Gamers", "Secretlab", "https://bucket-levelup.s3.us-east-1.amazonaws.com/silla_gamer.png"},
                {"Mouse Gamer Logitech G502 HERO", "Con sensor de alta precisión y botones personalizables, este mouse es ideal para gamers que buscan un control preciso y personalización.", "49990", "Mouse", "Logitech", "https://bucket-levelup.s3.us-east-1.amazonaws.com/mouse.png"},
                {"Mousepad Razer Goliathus Extended Chroma", "Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme para el movimiento del mouse.", "29990", "Mousepad", "Razer", "https://bucket-levelup.s3.us-east-1.amazonaws.com/mousepad.png"},
                {"Polera Gamer Personalizada 'Level-Up'", "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito.", "14990", "Poleras Personalizadas", "Level-Up", "https://bucket-levelup.s3.us-east-1.amazonaws.com/polera_gamer_life.png"}
        };
        
        for (String[] data : productosData) {
            Producto producto = new Producto();
            producto.setNombre(data[0]);
            producto.setDescripcion(data[1]);
            producto.setPrecio(new BigDecimal(data[2]));
            
            // Precio anterior aleatorio (10-20% más alto)
            BigDecimal precioAnterior = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(1.1 + (Math.random() * 0.1)))
                    .setScale(2, RoundingMode.HALF_UP);
            producto.setPrecioAnterior(precioAnterior);
            
            producto.setStock(faker.number().numberBetween(10, 100));
            producto.setImagen(data[5]);  // Usar la ruta de imagen del array
            producto.setMarca(data[4]);
            producto.setSku("SKU-" + faker.number().digits(8));
            producto.setDestacado(Math.random() > 0.7); // 30% destacados
            producto.setActivo(true);
            
            // Asignar categoría
            String catNombre = data[3];
            Categoria categoria = categorias.stream()
                    .filter(c -> c.getNombre().equals(catNombre))
                    .findFirst()
                    .orElseGet(categorias::getFirst);
            producto.setCategoria(categoria);
            
            productos.add(repository.save(producto));
        }
        
        // Productos adicionales aleatorios
        for (int i = 0; i < 20; i++) {
            Producto producto = new Producto();
            producto.setNombre(faker.commerce().productName());
            producto.setDescripcion(faker.lorem().paragraph());
            producto.setPrecio(new BigDecimal(faker.number().randomDouble(2, 19, 999)));
            producto.setPrecioAnterior(producto.getPrecio().multiply(BigDecimal.valueOf(1.15)));
            producto.setStock(faker.number().numberBetween(5, 150));
            producto.setImagen("/images/productos/Logo-Level-UP.png");
            producto.setMarca(faker.company().name());
            producto.setSku("SKU-" + faker.number().digits(8));
            producto.setDestacado(Math.random() > 0.8);
            producto.setActivo(true);
            producto.setCategoria(categorias.get(faker.number().numberBetween(0, categorias.size())));
            
            productos.add(repository.save(producto));
        }
        
        return productos;
    }
    
    private List<Blog> crearBlogs(BlogRepository repository) {
        List<Blog> blogs = new ArrayList<>();
        
        String[] titulos = {
                "Los mejores juegos de 2024",
                "Guía completa para principiantes en gaming",
                "PlayStation 5 vs Xbox Series X: Comparativa",
                "Tendencias en PC Gaming",
                "Cómo elegir el monitor perfecto para gaming"
        };
        
        for (String titulo : titulos) {
            Blog blog = new Blog();
            blog.setTitulo(titulo);
            blog.setResumen(faker.lorem().sentence(15));
            blog.setContenido(faker.lorem().paragraphs(5).toString());
            blog.setImagen("/images/blogs/" + titulo.toLowerCase().replace(" ", "-") + ".jpg");
            blog.setAutor(faker.name().fullName());
            blog.setPublicado(true);
            blogs.add(repository.save(blog));
        }
        
        // Blogs adicionales
        for (int i = 0; i < 5; i++) {
            Blog blog = new Blog();
            blog.setTitulo(faker.book().title());
            blog.setResumen(faker.lorem().sentence(12));
            blog.setContenido(faker.lorem().paragraphs(4).toString());
            blog.setImagen("/images/blogs/default.jpg");
            blog.setAutor(faker.name().fullName());
            blog.setPublicado(Math.random() > 0.3); // 70% publicados
            blogs.add(repository.save(blog));
        }
        
        return blogs;
    }
    
    private List<Contacto> crearContactos(ContactoRepository repository) {
        List<Contacto> contactos = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            Contacto contacto = new Contacto();
            contacto.setNombre(faker.name().fullName());
            contacto.setEmail(faker.internet().emailAddress());
            contacto.setTelefono(faker.phoneNumber().cellPhone());
            contacto.setAsunto(faker.lorem().sentence(5));
            contacto.setMensaje(faker.lorem().paragraph(3));
            contacto.setLeido(Math.random() > 0.5); // 50% leídos
            contactos.add(repository.save(contacto));
        }
        
        return contactos;
    }
}
