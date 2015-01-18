package evilcraft.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.infobook.InfoBookRegistry;
import evilcraft.infobook.InfoSection;
import evilcraft.item.OriginsOfDarkness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Gui for the Origins of Darkness book.
 * @author rubensworks
 */
public class GuiOriginsOfDarkness extends GuiScreen {

    protected static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, OriginsOfDarkness.getInstance().getGuiTexture());
    private static final int BUTTON_NEXT = 1;
    private static final int BUTTON_PREVIOUS = 2;

    protected final ItemStack itemStack;

    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;

    private InfoSection currentSection;
    private int page;
    private int guiWidth = 146;
    private int guiHeight = 180;
    private int left, top;

    public GuiOriginsOfDarkness(EntityPlayer player, int itemIndex) {
        itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex);
        currentSection = InfoBookRegistry.getInstance().getRoot();
        page = 0;
    }

    @Override
    public void initGui() {
        super.initGui();

        left = (width - guiWidth) / 2;
        top = (height - guiHeight) / 2;

        this.buttonList.add(this.buttonNextPage = new NextPageButton(BUTTON_NEXT, left + 105, top + 156, 0, 180));
        this.buttonList.add(this.buttonPreviousPage = new NextPageButton(BUTTON_PREVIOUS, left + 23, top + 156, 0, 193));
        this.updateGui();
    }

    @Override
    public void drawScreen(int f, int x, float y) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);

        drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
        currentSection.drawScreen(this, left, top, guiWidth, guiHeight, page);
        super.drawScreen(f, x, y);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }

    private void updateGui() {
        currentSection.bakeSection(getFontRenderer(), guiWidth, guiHeight / 12);
        updateButtons();
    }

    private void updateButtons() {
        this.buttonNextPage.visible = currentSection != null && ((page < currentSection.getPages() - 1) ||
                currentSection.getSubSections() > 0 ||
                !currentSection.isRoot() && currentSection.getParent().getSubSections() > currentSection.getChildIndex() + 1);
        this.buttonPreviousPage.visible = currentSection != null && (page > 0 ||
                !currentSection.isRoot() && currentSection.getChildIndex() >= 0);
    }

    protected void actionPerformed(GuiButton button) {
        if(button.id == BUTTON_NEXT && button.visible) {
            if(page < currentSection.getPages() - 1) {
                page++;
            } else if(currentSection.getSubSections() > 0) {
                currentSection = currentSection.getSubSection(0);
                page = 0;
            } else {
                currentSection = currentSection.getParent().getSubSection(currentSection.getChildIndex() + 1);
                page = 0;
            }
        } else if(button.id == BUTTON_PREVIOUS && button.visible) {
            if(page > 0) {
                page--;
            } else if(currentSection.getChildIndex() == 0) {
                currentSection = currentSection.getParent();
                page = 0;
            } else {
                currentSection = currentSection.getParent().getSubSection(currentSection.getChildIndex() - 1);
                page = 0;
            }
        } else {
            super.actionPerformed(button);
        }
        this.updateGui();
    }

    public void drawHorizontalRule(int x, int y) {
        int width = 88;
        int height = 10;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x - width / 2, y - height / 2, 146, 0, width, height);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton {

        private static final int WIDTH = 18;
        private static final int HEIGHT = 13;

        private int x;
        private int y;

        public NextPageButton(int id, int xPosition, int yPosition, int x, int y) {
            super(id, xPosition, yPosition, WIDTH, HEIGHT, "");
            this.x = x;
            this.y = y;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
            if (this.visible) {
                boolean isHover = mouseX >= this.xPosition && mouseY >= this.yPosition &&
                               mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                minecraft.getTextureManager().bindTexture(texture);
                int k = x;
                int l = y;

                if (isHover) {
                    k += WIDTH;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, WIDTH, HEIGHT);
            }
        }
    }

}
