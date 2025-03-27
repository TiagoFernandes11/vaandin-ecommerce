package study.course.VaadinStudy.view.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.view.components.AdminLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@RolesAllowed("ROLE_ADMIN")
@PageTitle("Administração produtos")
@Route(value = "/admin/products", layout = AdminLayout.class)
public class ManageProductsView extends VerticalLayout {

    private final ProdutoService produtoService;
    private Grid<Produto> listaProdutos;
    private byte[] imagemProduto;

    public ManageProductsView(ProdutoService produtoService){
        this.produtoService = produtoService;
        var titulo = new H3("Lista de produtos");
        var adicionarProdutoButton = new Button("Adicionar novo produto", event -> {
            abrirFormProduto(new Produto());
        });
        this.listaProdutos = new Grid<>(Produto.class, false);

        this.listaProdutos.addColumn(Produto::getId).setHeader("Id").setSortable(true);
        this.listaProdutos.addColumn(Produto::getNome).setHeader("Nome").setSortable(true);
        this.listaProdutos.addColumn(Produto::getPreco).setHeader("Valor").setSortable(true);
        this.listaProdutos.addColumn(Produto::getEstoque).setHeader("Estoque").setSortable(true);
        this.listaProdutos.addComponentColumn(produto -> {
            Button editarProduto = new Button("Editar", event -> {
                abrirFormProduto(produto);
            });
            Button excluirProduto = new Button("Excluir", event -> {
                abrirDialogoConfirmacaoExclusao(produto);
            });
            return new HorizontalLayout(editarProduto, excluirProduto);
        });

        atualizarLista();
        add(titulo, adicionarProdutoButton, listaProdutos);
    }

    private void abrirFormProduto(Produto produto){
        Dialog dialog = new Dialog();
        if(Objects.isNull(produto.getSku())){
            dialog.setHeaderTitle("Novo produto");
        } else{
            dialog.setHeaderTitle("Editar produto");
        }

        VerticalLayout verticalLayout = new VerticalLayout();

        TextField inputNome = new TextField("Nome");
        IntegerField inputSKU = new IntegerField("SKU: ");
        IntegerField inputEstoque = new IntegerField("Estoque: ");
        NumberField inputPreco = new NumberField("Preço: ");
        Text text = new Text("Imagem: ");
        Upload inputImagem = new Upload(new FileBuffer());

        inputImagem.setAcceptedFileTypes("image/jpeg", "image/png");
        inputImagem.addSucceededListener(event -> {
            FileBuffer fileBuffer = (FileBuffer) inputImagem.getReceiver();
            InputStream inputStream = fileBuffer.getInputStream();
            try {
                this.imagemProduto = inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        inputNome.setValue(produto.getNome() != null ? produto.getNome() : "");
        inputSKU.setValue(produto.getSku() != null ? produto.getSku() : 0);
        inputEstoque.setValue(produto.getEstoque() != null ? produto.getEstoque() : 0);
        inputPreco.setValue(produto.getPreco() != null ? produto.getPreco(): 0.00);

        Button enviarButton = new Button("Enviar", event -> {
            produto.setSku(inputSKU.getValue());
            produto.setNome(inputNome.getValue());
            produto.setEstoque(inputEstoque.getValue());
            produto.setPreco(inputPreco.getValue());
            produto.setImagem(this.imagemProduto);
            if(validarInput(produto)){
                if(produtoService.create(produto)){
                    Notification.show("Produto " + produto.getSku() + " salvo");
                    atualizarLista();
                    dialog.close();
                } else {
                    abrirDialogoDeErro("SKU " + produto.getSku() + " já esta cadastrado");
                }
            } else {
                    abrirDialogoDeErro("Todos os campos de texto devem ser preenchidos");
            }
        });

        verticalLayout.add(inputNome, inputSKU, inputEstoque, inputPreco, text, inputImagem, enviarButton);

        dialog.add(verticalLayout);

        dialog.open();
    }


    private boolean validarInput(Produto produto){
        return !produto.getNome().isBlank() && produto.getSku() > 0 && produto.getEstoque() != null && produto.getPreco() != null && produto.getPreco() > 0;
    }

    private void abrirDialogoDeErro(String erro){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Erro");

        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(erro);

        verticalLayout.add(new Button("Ok", event -> {
            dialog.close();
        }));

        dialog.add(verticalLayout);
        dialog.open();
    }

    private void abrirDialogoConfirmacaoExclusao(Produto produto){
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Confirmar exclusão");

        var verticalLayout = new VerticalLayout();

        var horizontalLayout = new HorizontalLayout();

        var botaoConfirmar = new Button("Confirmar", event -> {
            produtoService.delete(produto);
            Notification.show("Produto: " + produto.getSku() + " removido");
            dialog.close();
            atualizarLista();
        });

        var botaoCancelar = new Button("Cancelar", event -> {
            dialog.close();
        });

        horizontalLayout.add(botaoConfirmar, botaoCancelar);

        verticalLayout.add("Tem certeza de que quer excluir o usuario ?");
        verticalLayout.add(horizontalLayout);

        dialog.add(verticalLayout);

        dialog.open();
    }

    private void atualizarLista(){
        this.listaProdutos.setItems(produtoService.findAll());
    }
}
