package udemy.couse.VaadinStudy.view.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import udemy.couse.VaadinStudy.entities.Produto;
import udemy.couse.VaadinStudy.services.ProdutoService;

import java.io.IOException;
import java.io.InputStream;

@PageTitle("Manage products")
@Route(value = "/admin/products", layout = AdminLayout.class)
public class ManageProductsView extends VerticalLayout {

    private final ProdutoService produtoService;
    private Grid<Produto> listaProdutos;
    private byte[] imagemProduto;

    public ManageProductsView(ProdutoService produtoService){
        this.produtoService = produtoService;
        var titulo = new H3("Lista de produtos");
        var adicionarProdutoButton = new Button("Adicionar novo produto", event -> {
            abrirFormNovoProduto();
        });
        this.listaProdutos = new Grid<>(Produto.class, false);

        this.listaProdutos.addColumn(Produto::getId).setHeader("Id").setSortable(true);
        this.listaProdutos.addColumn(Produto::getNome).setHeader("Nome").setSortable(true);
        this.listaProdutos.addColumn(Produto::getPreco).setHeader("Valor").setSortable(true);
        this.listaProdutos.addColumn(Produto::getEstoque).setHeader("Estoque").setSortable(true);

        atualizarLista();
        add(titulo, adicionarProdutoButton, listaProdutos);
    }

    private void abrirFormNovoProduto(){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Novo produto");

        VerticalLayout verticalLayout = new VerticalLayout();

        TextField inputNome = new TextField("Nome");
        IntegerField inputSKU = new IntegerField("SKU: ");
        IntegerField inputEstoque = new IntegerField("Estoque: ");
        NumberField inputPreco = new NumberField("Preço: ");
        Upload inputImagem = new Upload(new FileBuffer());
        inputImagem.addSucceededListener(event -> {
            FileBuffer fileBuffer = (FileBuffer) inputImagem.getReceiver();
            InputStream inputStream = fileBuffer.getInputStream();
            try {
                this.imagemProduto = inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Button enviarButton = new Button("Enviar", event -> {
            Produto produto = new Produto();
            produto.setSku(inputSKU.getValue());
            produto.setNome(inputNome.getValue());
            produto.setEstoque(inputEstoque.getValue());
            produto.setPreco(inputPreco.getValue());
            produto.setImagem(this.imagemProduto);
            if(validarInput(produto)){
                if(produtoService.create(produto)){
                    Notification.show("Produto " + produto.getSku() + " criado");
                    atualizarLista();
                    dialog.close();
                } else {
                    abrirDialogoDeErro("SKU " + produto.getSku() + " já esta cadastrado");
                }
            } else {
                    abrirDialogoDeErro("Todos os campos de texto devem ser preenchidos");
            }
        });

        verticalLayout.add(inputNome, inputSKU, inputEstoque, inputPreco, inputImagem, enviarButton);

        dialog.add(verticalLayout);

        dialog.open();
    }

    private boolean validarInput(Produto produto){
        if(!produto.getNome().isBlank() && produto.getSku() > 0 && produto.getEstoque() != null && produto.getPreco() != null && produto.getPreco() > 0){
            return true;
        }
        return false;
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

    private void atualizarLista(){
        this.listaProdutos.setItems(produtoService.findAll());
    }
}
