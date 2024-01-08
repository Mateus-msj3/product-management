package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.exception.NotFoundException;
import io.github.msj.productmanagement.model.dto.CategoriaDTO;
import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.model.dto.ProdutoRequestDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Categoria;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.model.enums.TipoCategoria;
import io.github.msj.productmanagement.model.enums.TipoPermissao;
import io.github.msj.productmanagement.repository.CategoriaRepository;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import io.github.msj.productmanagement.security.user.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ConfiguracaoCamposService configuracaoCamposService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ProdutoService produtoService;


    @Nested
    class CriarProduto {

        @Test
        @DisplayName("Deve criar um Produto com sucesso quando o usuário é um estoquista")
        void deveSalvarComSucessoQuandoUsuarioEstoquista() {
            var produtoRequest = ProdutoRequestDTO.builder()
                    .nome("Produto Teste")
                    .ativo(true)
                    .sku("SKU123")
                    .categoria(CategoriaDTO.builder()
                            .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build())
                    .valorCusto(BigDecimal.valueOf(10.00))
                    .icms(BigDecimal.valueOf(0.18))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem.jpg")
                    .quantidadeEstoque(100)
                    .build();

            var categoria = new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL);

            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(categoria);
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(produtoRequest.getAtivo());
            produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
            produto.setNome(produtoRequest.getNome());
            produto.setImagemProduto(produtoRequest.getImagemProduto());
            produto.setIcms(produtoRequest.getIcms());
            produto.setSku(produtoRequest.getSku());
            produto.setValorCusto(produtoRequest.getValorCusto());
            produto.setValorVenda(produtoRequest.getValorVenda());
            produto.setCriadoPor("Estoquista");

            when(categoriaRepository.findById(produtoRequest.getCategoria().getId()))
                    .thenReturn(Optional.of(categoria));

            when(modelMapper.map(produtoRequest, Produto.class)).thenReturn(produto);

            when(usuarioService.obterUsuarioLogado())
                    .thenReturn(new UserDetailsImpl(1L, "Estoquista", "e@email.com", null,
                            Collections.singletonList(new SimpleGrantedAuthority(TipoPermissao.ROLE_ESTOQUISTA.name()))));

            when(produtoRepository.save(produto)).thenReturn(produto);

            when(usuarioService.isEstoquista()).thenReturn(true);

            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(1L, List.of("icms", "valorCusto")));

            var retorno = produtoService.salvar(produtoRequest);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(null, retorno.getIcms(), "O ICMS deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
            assertEquals(null, retorno.getValorCusto(), "O valor de custo deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
        }

        @Test
        @DisplayName("Deve criar um Produto com sucesso quando o usuário é um administrador")
        void deveSalvarComSucessoQuandoUsuarioAdministrador() {
            var categoriaDTO = CategoriaDTO.builder()
                    .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build();

            var produtoRequest = ProdutoRequestDTO.builder()
                    .nome("Produto Teste")
                    .ativo(true)
                    .sku("SKU123")
                    .categoria(categoriaDTO)
                    .valorCusto(BigDecimal.valueOf(10.00))
                    .icms(BigDecimal.valueOf(0.18))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem.jpg")
                    .quantidadeEstoque(100)
                    .build();

            var categoria = new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL);

            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(categoria);
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(produtoRequest.getAtivo());
            produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
            produto.setNome(produtoRequest.getNome());
            produto.setImagemProduto(produtoRequest.getImagemProduto());
            produto.setIcms(produtoRequest.getIcms());
            produto.setSku(produtoRequest.getSku());
            produto.setValorCusto(produtoRequest.getValorCusto());
            produto.setValorVenda(produtoRequest.getValorVenda());
            produto.setCriadoPor("Estoquista");

            var produtoResponse = ProdutoResponseDTO.builder()
                    .id(produto.getId())
                    .nome(produto.getNome())
                    .dataCadastro(produto.getDataCadastro())
                    .ativo(produto.getAtivo())
                    .sku(produto.getSku())
                    .imagemProduto(produto.getImagemProduto())
                    .valorCusto(produto.getValorCusto())
                    .valorVenda(produto.getValorVenda())
                    .icms(produto.getIcms())
                    .quantidadeEstoque(produto.getQuantidadeEstoque())
                    .categoria(categoriaDTO)
                    .build();

            when(categoriaRepository.findById(produtoRequest.getCategoria().getId()))
                    .thenReturn(Optional.of(categoria));

            when(modelMapper.map(produtoRequest, Produto.class)).thenReturn(produto);

            when(usuarioService.obterUsuarioLogado())
                    .thenReturn(new UserDetailsImpl(1L, "Administrador", "admin@email.com",
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(TipoPermissao.ROLE_ADMIN.name()))));

            when(produtoRepository.save(produto)).thenReturn(produto);

            when(usuarioService.isEstoquista()).thenReturn(false);

            when(modelMapper.map(produto, ProdutoResponseDTO.class)).thenReturn(produtoResponse);

            var retorno = produtoService.salvar(produtoRequest);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(produto.getIcms(), retorno.getIcms(), "O ICMS não deve ser nulo, " +
                    "pois o usuário admnistrador pode visualizar todos os campos");
            assertEquals(produto.getValorCusto(), retorno.getValorCusto(), "O valor de custo não deve ser nulo,"
                    + " pois o usuário admnistrador pode visualizar todos os campos");
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar salvar um produto quando a categoria não existir")
        void deveLancarExecacaoAoTentarSalvarECategoriaNaoExistir() {
            var categoriaDTO = CategoriaDTO.builder()
                    .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build();

            var produtoRequest = ProdutoRequestDTO.builder()
                    .nome("Produto Teste")
                    .ativo(true)
                    .sku("SKU123")
                    .categoria(categoriaDTO)
                    .valorCusto(BigDecimal.valueOf(10.00))
                    .icms(BigDecimal.valueOf(0.18))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem.jpg")
                    .quantidadeEstoque(100)
                    .build();

            when(categoriaRepository.findById(produtoRequest.getCategoria().getId()))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> produtoService.salvar(produtoRequest));

        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar salvar um produto")
        void deveLancarExecacaoAoTentarSalvar() {
            var categoriaDTO = CategoriaDTO.builder()
                    .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build();

            var produtoRequest = ProdutoRequestDTO.builder()
                    .nome("Produto Teste")
                    .ativo(true)
                    .sku("SKU123")
                    .categoria(categoriaDTO)
                    .valorCusto(BigDecimal.valueOf(10.00))
                    .icms(BigDecimal.valueOf(0.18))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem.jpg")
                    .quantidadeEstoque(100)
                    .build();

            var categoria = new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL);

            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(categoria);
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(produtoRequest.getAtivo());
            produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
            produto.setNome(produtoRequest.getNome());
            produto.setImagemProduto(produtoRequest.getImagemProduto());
            produto.setIcms(produtoRequest.getIcms());
            produto.setSku(produtoRequest.getSku());
            produto.setValorCusto(produtoRequest.getValorCusto());
            produto.setValorVenda(produtoRequest.getValorVenda());
            produto.setCriadoPor("Estoquista");

            when(categoriaRepository.findById(produtoRequest.getCategoria().getId()))
                    .thenReturn(Optional.of(categoria));

            when(modelMapper.map(produtoRequest, Produto.class)).thenReturn(produto);

            when(usuarioService.obterUsuarioLogado())
                    .thenReturn(new UserDetailsImpl(1L, "Estoquista", "e@email.com", null,
                            Collections.singletonList(new SimpleGrantedAuthority(TipoPermissao.ROLE_ESTOQUISTA.name()))));

            when(produtoRepository.save(produto)).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> produtoService.salvar(produtoRequest));

        }

    }

    @Nested
    class EditarProduto {

        @Test
        @DisplayName("Deve editar um Produto com sucesso quando o usuário é um estoquista")
        void deveEditarComSucessoQuandoUsuarioEstoquista() {
            var produtoRequest = ProdutoRequestDTO.builder()
                    .id(1L)
                    .nome("Produto Teste com edição")
                    .ativo(true)
                    .sku("SKU123402")
                    .categoria(CategoriaDTO.builder()
                            .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build())
                    .valorCusto(BigDecimal.valueOf(15.00))
                    .icms(BigDecimal.valueOf(0.20))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem2.jpg")
                    .quantidadeEstoque(100)
                    .build();

            var categoria = new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL);

            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(categoria);
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(produtoRequest.getAtivo());
            produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
            produto.setNome(produtoRequest.getNome());
            produto.setImagemProduto(produtoRequest.getImagemProduto());
            produto.setIcms(produtoRequest.getIcms());
            produto.setSku(produtoRequest.getSku());
            produto.setValorCusto(produtoRequest.getValorCusto());
            produto.setValorVenda(produtoRequest.getValorVenda());
            produto.setCriadoPor("Estoquista");

            when(produtoRepository.findById(produtoRequest.getId())).thenReturn(Optional.of(produto));

            when(produtoRepository.save(produto)).thenReturn(produto);

            when(usuarioService.isEstoquista()).thenReturn(true);

            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(1L, List.of("icms", "valorCusto")));

            var retorno = produtoService.editar(1L, produtoRequest);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(null, retorno.getIcms(), "O ICMS deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
            assertEquals(null, retorno.getValorCusto(), "O valor de custo deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
        }

        @Test
        @DisplayName("Deve editar um Produto com sucesso quando o usuário é um Administrador")
        void deveEditarComSucessoQuandoUsuarioAdministrador() {
            var categoriaDTO = CategoriaDTO.builder()
                    .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build();

            var produtoRequest = ProdutoRequestDTO.builder()
                    .id(1L)
                    .nome("Produto Teste com edição")
                    .ativo(true)
                    .sku("SKU123402")
                    .categoria(CategoriaDTO.builder()
                            .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build())
                    .valorCusto(BigDecimal.valueOf(15.00))
                    .icms(BigDecimal.valueOf(0.20))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem2.jpg")
                    .quantidadeEstoque(100)
                    .build();

            var categoria = new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL);

            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(categoria);
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(produtoRequest.getAtivo());
            produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
            produto.setNome(produtoRequest.getNome());
            produto.setImagemProduto(produtoRequest.getImagemProduto());
            produto.setIcms(produtoRequest.getIcms());
            produto.setSku(produtoRequest.getSku());
            produto.setValorCusto(produtoRequest.getValorCusto());
            produto.setValorVenda(produtoRequest.getValorVenda());
            produto.setCriadoPor("Estoquista");

            var produtoResponse = ProdutoResponseDTO.builder()
                    .id(produto.getId())
                    .nome(produto.getNome())
                    .dataCadastro(produto.getDataCadastro())
                    .ativo(produto.getAtivo())
                    .sku(produto.getSku())
                    .imagemProduto(produto.getImagemProduto())
                    .valorCusto(produto.getValorCusto())
                    .valorVenda(produto.getValorVenda())
                    .icms(produto.getIcms())
                    .quantidadeEstoque(produto.getQuantidadeEstoque())
                    .categoria(categoriaDTO)
                    .criadoPor(produto.getCriadoPor())
                    .build();

            when(produtoRepository.findById(produtoRequest.getId())).thenReturn(Optional.of(produto));

            when(produtoRepository.save(produto)).thenReturn(produto);

            when(usuarioService.isEstoquista()).thenReturn(false);

            when(modelMapper.map(produto, ProdutoResponseDTO.class)).thenReturn(produtoResponse);

            var retorno = produtoService.editar(1L, produtoRequest);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(produto.getIcms(), retorno.getIcms(), "O ICMS não deve ser nulo, " +
                    "por causa da configuração de visualização de campos que não se aplica ao Administrador");
            assertEquals(produto.getValorCusto(), retorno.getValorCusto(),
                    "O valor de custo não deve ser nulo, " +
                    "por causa da configuração de visualização de campos que não se aplica ao Administrador");
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar editar um produto quando não existir")
        void deveLancarExecacaoAoTentarEditararEProdutoNaoExistir() {
            var categoriaDTO = CategoriaDTO.builder()
                    .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build();

            var produtoRequest = ProdutoRequestDTO.builder()
                    .id(1L)
                    .nome("Produto Teste")
                    .ativo(true)
                    .sku("SKU123")
                    .categoria(categoriaDTO)
                    .valorCusto(BigDecimal.valueOf(10.00))
                    .icms(BigDecimal.valueOf(0.18))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem.jpg")
                    .quantidadeEstoque(100)
                    .build();

            when(produtoRepository.findById(produtoRequest.getId()))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> produtoService.editar(1L, produtoRequest));

        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar editar um produto")
        void deveLancarExecacaoAoTentarEditar() {
            var produtoRequest = ProdutoRequestDTO.builder()
                    .id(1L)
                    .nome("Produto Teste com edição")
                    .ativo(true)
                    .sku("SKU123402")
                    .categoria(CategoriaDTO.builder()
                            .id(1L).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build())
                    .valorCusto(BigDecimal.valueOf(15.00))
                    .icms(BigDecimal.valueOf(0.20))
                    .valorVenda(BigDecimal.valueOf(12.00))
                    .imagemProduto("imagem2.jpg")
                    .quantidadeEstoque(100)
                    .build();

            var categoria = new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL);

            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(categoria);
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(produtoRequest.getAtivo());
            produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
            produto.setNome(produtoRequest.getNome());
            produto.setImagemProduto(produtoRequest.getImagemProduto());
            produto.setIcms(produtoRequest.getIcms());
            produto.setSku(produtoRequest.getSku());
            produto.setValorCusto(produtoRequest.getValorCusto());
            produto.setValorVenda(produtoRequest.getValorVenda());
            produto.setCriadoPor("Estoquista");

            when(produtoRepository.findById(produtoRequest.getId())).thenReturn(Optional.of(produto));

            when(produtoRepository.save(produto)).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> produtoService.editar(1L, produtoRequest));

        }

    }

    @Nested
    class ListarProduto {

        @Test
        @DisplayName("Deve listar todos os  Produtos com sucesso quando o usuário é um estoquista")
        void deveListarTodosProdutosComSucessoQuandoUsuarioEstoquista() {
            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL));
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(true);
            produto.setQuantidadeEstoque(100);
            produto.setNome("Produto Teste");
            produto.setImagemProduto("imagem.jpg");
            produto.setIcms(BigDecimal.valueOf(0.20));
            produto.setSku("SKU123402");
            produto.setValorCusto(BigDecimal.valueOf(15.00));
            produto.setValorVenda(BigDecimal.valueOf(12.00));
            produto.setCriadoPor("Estoquista");

            var produtos = List.of(produto);

            when(produtoRepository.findAll()).thenReturn(produtos);

            when(usuarioService.isEstoquista()).thenReturn(true);

            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(1L, List.of("icms", "valorCusto")));

            var retorno = produtoService.listarTodos();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(produtos.size(), retorno.size());
        }

        @Test
        @DisplayName("Deve listar todos os produtos com sucesso quando o usuário é um Administrador")
        void deveListarTodosProdutosComSucessoQuandoUsuarioAdministrador() {
            var produto = new Produto();
            produto.setId(1L);
            produto.setCategoria(new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL));
            produto.setDataCadastro(LocalDate.now());
            produto.setAtivo(true);
            produto.setQuantidadeEstoque(100);
            produto.setNome("Produto Teste");
            produto.setImagemProduto("imagem.jpg");
            produto.setIcms(BigDecimal.valueOf(0.20));
            produto.setSku("SKU123402");
            produto.setValorCusto(BigDecimal.valueOf(15.00));
            produto.setValorVenda(BigDecimal.valueOf(12.00));
            produto.setCriadoPor("Estoquista");

            var produtos = List.of(produto);

            when(produtoRepository.findAll()).thenReturn(produtos);

            when(usuarioService.isEstoquista()).thenReturn(false);

            when(modelMapper.map(produto, ProdutoResponseDTO.class)).thenReturn(new ProdutoResponseDTO());

            var retorno = produtoService.listarTodos();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(produtos.size(), retorno.size());
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia ao tentar listar todos os produtos")
        void deveRetornarUmaListaVaziaAoListarTodosProdutos() {

            when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

            var retorno = produtoService.listarTodos();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(0, retorno.size());

        }

        @Test
        @DisplayName("Deve lançar ao tentar listar todos os produtos")
        void deveLancarExecacaoAoTentarListarTosProdutos() {
            when(produtoRepository.findAll()).thenThrow(RuntimeException.class);
            assertThrows(RuntimeException.class, () -> produtoService.listarTodos());
        }

    }
}