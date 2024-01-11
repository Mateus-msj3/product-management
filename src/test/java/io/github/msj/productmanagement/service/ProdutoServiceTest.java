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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    private final Long ID = 1L;

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

    @Captor
    private ArgumentCaptor<Produto> produtoArgumentCaptor;

    @InjectMocks
    private ProdutoService produtoService;

    private Categoria categoria;

    private CategoriaDTO categoriaDTO;

    private Produto produto;

    private ProdutoRequestDTO produtoRequest;

    private ProdutoResponseDTO produtoResponse;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1L, "Categoria Teste", true, TipoCategoria.NORMAL);

        categoriaDTO = CategoriaDTO.builder()
                .id(ID).nome("Categoria Teste").ativo(true).tipo(TipoCategoria.NORMAL).build();

        produtoRequest = ProdutoRequestDTO.builder()
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

        produto = new Produto();
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

        produtoResponse = ProdutoResponseDTO.builder()
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

    }


    @Nested
    class CriarProduto {

        @Test
        @DisplayName("Deve criar um Produto com sucesso quando o usuário é um estoquista")
        void deveSalvarComSucessoQuandoUsuarioEstoquista() {
            when(categoriaRepository.findById(ID)).thenReturn(Optional.of(categoria));
            when(modelMapper.map(produtoRequest, Produto.class)).thenReturn(produto);
            when(usuarioService.obterUsuarioLogado())
                    .thenReturn(new UserDetailsImpl(ID, "Estoquista", "e@email.com", null,
                            Collections.singletonList(new SimpleGrantedAuthority(TipoPermissao.ROLE_ESTOQUISTA.name()))));
            when(produtoRepository.save(produtoArgumentCaptor.capture())).thenReturn(produto);
            when(usuarioService.isEstoquista()).thenReturn(true);
            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(ID, List.of("icms", "valorCusto")));

            var retorno = produtoService.salvar(produtoRequest);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertNull(retorno.getIcms(), "O ICMS deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
            assertNull(retorno.getValorCusto(), "O valor de custo deve ser nulo, " +
                    "por causa da configuração de visualização de campos");

            verify(categoriaRepository, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(produtoRequest, Produto.class);
            verify(usuarioService, times(1)).obterUsuarioLogado();
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
            verify(usuarioService, times(1)).isEstoquista();
            verify(configuracaoCamposService, times(1)).obterConfiguracao();
        }

        @Test
        @DisplayName("Deve criar um Produto com sucesso quando o usuário é um administrador")
        void deveSalvarComSucessoQuandoUsuarioAdministrador() {
            when(categoriaRepository.findById(ID)).thenReturn(Optional.of(categoria));
            when(modelMapper.map(produtoRequest, Produto.class)).thenReturn(produto);
            when(usuarioService.obterUsuarioLogado())
                    .thenReturn(new UserDetailsImpl(ID, "Administrador", "admin@email.com",
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(TipoPermissao.ROLE_ADMIN.name()))));
            when(produtoRepository.save(produtoArgumentCaptor.capture())).thenReturn(produto);
            when(usuarioService.isEstoquista()).thenReturn(false);
            when(modelMapper.map(produto, ProdutoResponseDTO.class)).thenReturn(produtoResponse);

            var retorno = produtoService.salvar(produtoRequest);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(produto.getIcms(), retorno.getIcms(), "O ICMS não deve ser nulo, " +
                    "pois o usuário admnistrador pode visualizar todos os campos");
            assertEquals(produto.getValorCusto(), retorno.getValorCusto(), "O valor de custo não deve ser nulo,"
                    + " pois o usuário admnistrador pode visualizar todos os campos");

            verify(categoriaRepository, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(produtoRequest, Produto.class);
            verify(usuarioService, times(1)).obterUsuarioLogado();
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
            verify(usuarioService, times(1)).isEstoquista();
            verify(modelMapper, times(1)).map(produto, ProdutoResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar salvar um produto quando a categoria não existir")
        void deveLancarExecacaoAoTentarSalvarECategoriaNaoExistir() {
            when(categoriaRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> produtoService.salvar(produtoRequest));

            verify(categoriaRepository, times(1)).findById(ID);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar salvar um produto")
        void deveLancarExecacaoAoTentarSalvar() {
            when(categoriaRepository.findById(ID)).thenReturn(Optional.of(categoria));
            when(modelMapper.map(produtoRequest, Produto.class)).thenReturn(produto);
            when(usuarioService.obterUsuarioLogado())
                    .thenReturn(new UserDetailsImpl(ID, "Estoquista", "e@email.com", null,
                            Collections.singletonList(new SimpleGrantedAuthority(TipoPermissao.ROLE_ESTOQUISTA.name()))));
            when(produtoRepository.save(produtoArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> produtoService.salvar(produtoRequest));

            verify(categoriaRepository, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(produtoRequest, Produto.class);
            verify(usuarioService, times(1)).obterUsuarioLogado();
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
        }

    }

    @Nested
    class EditarProduto {

        @Test
        @DisplayName("Deve editar um Produto com sucesso quando o usuário é um estoquista")
        void deveEditarComSucessoQuandoUsuarioEstoquista() {
            produtoRequest.setNome("Produto Teste com edição");
            produtoRequest.setSku("SKU123402");

            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(produtoArgumentCaptor.capture())).thenReturn(produto);
            when(usuarioService.isEstoquista()).thenReturn(true);
            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(ID, List.of("icms", "valorCusto")));

            var retorno = produtoService.editar(ID, produtoRequest);
            var produtoCapturado = produtoArgumentCaptor.getValue();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(retorno.getNome(), produtoCapturado.getNome());
            assertEquals(retorno.getSku(), produtoCapturado.getSku());
            assertNull(retorno.getIcms(), "O ICMS deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
            assertNull(retorno.getValorCusto(), "O valor de custo deve ser nulo, " +
                    "por causa da configuração de visualização de campos");

            verify(produtoRepository, times(1)).findById(ID);
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
            verify(usuarioService, times(2)).isEstoquista();
            verify(configuracaoCamposService, times(1)).obterConfiguracao();
        }

        @Test
        @DisplayName("Deve editar um Produto com sucesso quando alterar a categoria")
        void deveEditarComSucessoQuandoAlterarCategoria() {
            produtoRequest.setNome("Produto Teste com edição");
            produtoRequest.setSku("SKU123402");
            produtoRequest.setCategoria(new CategoriaDTO(2L, null, null, null));

            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            when(categoriaRepository.findById(2L)).thenReturn(Optional.of(new Categoria(2L, "Categoria 2",
                    true, TipoCategoria.ESPECIAL)));
            when(produtoRepository.save(produtoArgumentCaptor.capture())).thenReturn(produto);
            when(usuarioService.isEstoquista()).thenReturn(true);
            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(ID, List.of("icms", "valorCusto")));

            var retorno = produtoService.editar(ID, produtoRequest);
            var produtoCapturado = produtoArgumentCaptor.getValue();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(retorno.getNome(), produtoCapturado.getNome());
            assertEquals(retorno.getSku(), produtoCapturado.getSku());
            assertNull(retorno.getIcms(), "O ICMS deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
            assertNull(retorno.getValorCusto(), "O valor de custo deve ser nulo, " +
                    "por causa da configuração de visualização de campos");

            verify(produtoRepository, times(1)).findById(ID);
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
            verify(usuarioService, times(2)).isEstoquista();
            verify(configuracaoCamposService, times(1)).obterConfiguracao();
        }

        @Test
        @DisplayName("Deve editar um Produto com sucesso quando o usuário é um Administrador")
        void deveEditarComSucessoQuandoUsuarioAdministrador() {
            var produto = new Produto();
            produto.setId(ID);
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

            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(produtoArgumentCaptor.capture())).thenReturn(produto);
            when(usuarioService.isEstoquista()).thenReturn(false);
            when(modelMapper.map(produto, ProdutoResponseDTO.class)).thenReturn(produtoResponse);

            var retorno = produtoService.editar(ID, produtoRequest);
            var produtoCapturado = produtoArgumentCaptor.getValue();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(retorno.getNome(), produtoCapturado.getNome());
            assertEquals(retorno.getSku(), produtoCapturado.getSku());
            assertEquals(produto.getIcms(), retorno.getIcms(), "O ICMS não deve ser nulo, " +
                    "por causa da configuração de visualização de campos que não se aplica ao Administrador");
            assertEquals(produto.getValorCusto(), retorno.getValorCusto(),
                    "O valor de custo não deve ser nulo, " +
                    "por causa da configuração de visualização de campos que não se aplica ao Administrador");

            verify(produtoRepository, times(1)).findById(ID);
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
            verify(usuarioService, times(2)).isEstoquista();
            verify(modelMapper, times(1)).map(produto, ProdutoResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar editar um produto quando não existir")
        void deveLancarExecacaoAoTentarEditararEProdutoNaoExistir() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> produtoService.editar(ID, produtoRequest));

            verify(produtoRepository, times(1)).findById(ID);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar editar um produto")
        void deveLancarExecacaoAoTentarEditar() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(produto)).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> produtoService.editar(ID, produtoRequest));

            verify(produtoRepository, times(1)).findById(ID);
            verify(produtoRepository, times(1)).save(produto);
        }

    }

    @Nested
    class ListarProduto {

        @Test
        @DisplayName("Deve listar todos os  Produtos com sucesso quando o usuário é um estoquista")
        void deveListarTodosProdutosComSucessoQuandoUsuarioEstoquista() {
            var produtos = List.of(produto);

            when(produtoRepository.findAll()).thenReturn(produtos);
            when(usuarioService.isEstoquista()).thenReturn(true);
            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(ID, List.of("icms", "valorCusto")));

            var retorno = produtoService.listarTodos();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(produtos.size(), retorno.size());

            verify(produtoRepository, times(1)).findAll();
            verify(usuarioService, times(1)).isEstoquista();
            verify(configuracaoCamposService, times(1)).obterConfiguracao();
        }

        @Test
        @DisplayName("Deve listar todos os produtos com sucesso quando o usuário é um Administrador")
        void deveListarTodosProdutosComSucessoQuandoUsuarioAdministrador() {
            var produtos = List.of(produto);

            when(produtoRepository.findAll()).thenReturn(produtos);
            when(usuarioService.isEstoquista()).thenReturn(false);
            when(modelMapper.map(produto, ProdutoResponseDTO.class)).thenReturn(new ProdutoResponseDTO());

            var retorno = produtoService.listarTodos();

            assertNotNull(retorno, "O retorno nãmo deve ser nulo");
            assertEquals(produtos.size(), retorno.size());

            verify(produtoRepository, times(1)).findAll();
            verify(usuarioService, times(1)).isEstoquista();
            verify(modelMapper, times(1)).map(produto, ProdutoResponseDTO.class);
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia ao tentar listar todos os produtos")
        void deveRetornarUmaListaVaziaAoListarTodosProdutos() {
            when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

            var retorno = produtoService.listarTodos();

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(0, retorno.size());

            verify(produtoRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar listar todos os produtos")
        void deveLancarExecacaoAoTentarListarTodossProdutos() {
            when(produtoRepository.findAll()).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> produtoService.listarTodos());

            verify(produtoRepository, times(1)).findAll();
        }

    }

    @Nested
    class ListarProdutoPorId {

        @Test
        @DisplayName("Deve listar Produto por id com sucesso quando o usuário é um estoquista")
        void deveListarProdutoPorIdComSucessoQuandoUsuarioEstoquista() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            when(usuarioService.isEstoquista()).thenReturn(true);
            when(configuracaoCamposService.obterConfiguracao())
                    .thenReturn(new ConfiguracaoCamposDTO(ID, List.of("icms", "valorCusto")));

            var retorno = produtoService.buscarPorId(ID);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertNull(retorno.getIcms(), "O ICMS deve ser nulo, " +
                    "por causa da configuração de visualização de campos");
            assertNull(retorno.getValorCusto(), "O valor de custo deve ser nulo, " +
                    "por causa da configuração de visualização de campos");

            verify(produtoRepository, times(1)).findById(ID);
            verify(usuarioService, times(1)).isEstoquista();
            verify(configuracaoCamposService, times(1)).obterConfiguracao();
        }

        @Test
        @DisplayName("Deve listar Produto por id com sucesso quando o usuário é um Administrador")
        void deveListarProdutoPorIdComSucessoQuandoUsuarioAdministrador() {
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

            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            when(usuarioService.isEstoquista()).thenReturn(false);
            when(modelMapper.map(produto, ProdutoResponseDTO.class)).thenReturn(produtoResponse);

            var retorno = produtoService.buscarPorId(ID);

            assertNotNull(retorno, "O retorno não deve ser nulo");
            assertEquals(produto.getIcms(), retorno.getIcms(), "O ICMS não deve ser nulo, " +
                    "por causa da configuração de visualização de campos que não se aplica ao Administrador");
            assertEquals(produto.getValorCusto(), retorno.getValorCusto(),
                    "O valor de custo não deve ser nulo, " +
                            "por causa da configuração de visualização de campos que não se aplica ao Administrador");

            verify(produtoRepository, times(1)).findById(ID);
            verify(usuarioService, times(1)).isEstoquista();
            verify(modelMapper, times(1)).map(produto, ProdutoResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar buscar um produto por id e o produto não existir")
        void deveLancarExecacaoAoTentarEditararEProdutoNaoExistir() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> produtoService.buscarPorId(ID));

            verify(produtoRepository, times(1)).findById(ID);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar listar um produto por id")
        void deveLancarExecacaoAoTentarListarProdudoPorId() {
            when(produtoRepository.findById(ID)).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> produtoService.buscarPorId(ID));

            verify(produtoRepository, times(1)).findById(produto.getId());
        }

    }

    @Nested
    class DeletarProduto {

        @Test
        @DisplayName("Deve deletar Produto com sucesso")
        void deveDeletarProdutoComSucesso() {
            when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
            doNothing().when(produtoRepository).delete(produto);

            produtoService.deletar(ID);

            verify(produtoRepository, times(1)).findById(ID);
            verify(produtoRepository, times(1)).delete(produto);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar deletar um produto e o produto não existir")
        void deveLancarExecacaoAoTentarDeletarEProdutoNaoExistir() {
            when(produtoRepository.findById(ID))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> produtoService.deletar(ID));

            verify(produtoRepository, times(1)).findById(ID);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar deletar um produto")
        void deveLancarExecacaoAoTentarDeletarProdudo() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            doThrow(RuntimeException.class).when(produtoRepository).delete(produto);

            assertThrows(RuntimeException.class, () -> produtoService.deletar(ID));

            verify(produtoRepository, times(1)).findById(ID);
        }

    }

    @Nested
    class InativarProduto {

        @Test
        @DisplayName("Deve deletar Produto com sucesso")
        void deveInativarProdutoComSucesso() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            doReturn(produto).when(produtoRepository).save(produtoArgumentCaptor.capture());

            produtoService.inativar(ID);

            var produtoCapturado = produtoArgumentCaptor.getValue();

            assertEquals(produto.getAtivo(), produtoCapturado.getAtivo());

            verify(produtoRepository, times(1)).findById(ID);
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar inativar um produto e o produto não existir")
        void deveLancarExecacaoAoTentarInativarEProdutoNaoExistir() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> produtoService.inativar(ID));

            verify(produtoRepository, times(1)).findById(ID);
        }

        @Test
        @DisplayName("Deve lançar uma execeção ao tentar inativar um produto")
        void deveLancarExecacaoAoTentarInativarProdudo() {
            when(produtoRepository.findById(ID)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(produto)).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> produtoService.inativar(ID));

            verify(produtoRepository, times(1)).findById(ID);
            verify(produtoRepository, times(1)).save(produto);
        }

    }


}