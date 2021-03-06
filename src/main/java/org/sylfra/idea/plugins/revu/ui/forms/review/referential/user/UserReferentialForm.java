package org.sylfra.idea.plugins.revu.ui.forms.review.referential.user;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sylfra.idea.plugins.revu.RevuBundle;
import org.sylfra.idea.plugins.revu.model.Review;
import org.sylfra.idea.plugins.revu.model.User;
import org.sylfra.idea.plugins.revu.ui.forms.review.referential.AbstractDetailDialog;
import org.sylfra.idea.plugins.revu.ui.forms.review.referential.AbstractReferentialDetailForm;
import org.sylfra.idea.plugins.revu.ui.forms.review.referential.AbstractReferentialForm;
import org.sylfra.idea.plugins.revu.ui.forms.review.referential.ReferentialListHolder;

import javax.swing.*;

/**
 * @author <a href="mailto:syllant@gmail.com">Sylvain FRANCOIS</a>
 * @version $Id$
 */
public class UserReferentialForm extends AbstractReferentialForm<User>
{
  public UserReferentialForm(Project project)
  {
    super(project);
  }

  protected boolean isTableSelectionMovable()
  {
    return false;
  }

  @Override
  protected void internalValidateInput(@Nullable ReferentialListHolder<User> data)
  {
    super.internalValidateInput(data);

    // Check if current user is contained in list
    boolean adminFound;

    Review enclosingReview = getEnclosingReview(data);
    if ((data == null) || (enclosingReview == null) || (enclosingReview.isEmbedded()))
    {
      adminFound = true;
    }
    else
    {
      adminFound = false;

      for (User user : table.getListTableModel().getItems())
      {
        if (user.getRoles().contains(User.Role.ADMIN))
        {
          adminFound = true;
        }
      }
    }

    updateError(table, !adminFound,
      RevuBundle.message("projectSettings.review.referential.user.form.adminNotFound.message"));
  }

  protected IDetailDialogFactory<User> buildDetailDialogFactory()
  {
    return new IDetailDialogFactory<User>()
    {
      @NotNull
      public AbstractDetailDialog<User> createDialog()
      {
        return new AbstractDetailDialog<User>()
        {
          protected AbstractReferentialDetailForm<User> buildNestedForm()
          {
            return new UserDetailForm(table);
          }

          @Nls
          protected String getTitleKey(boolean addMode)
          {
            return addMode
              ? "projectSettings.review.referential.user.addDialog.title"
              : "projectSettings.review.referential.user.editDialog.title";
          }

          @NotNull
          protected User createDefaultData()
          {
            return new User();
          }
        };
      }
    };
  }

  protected ReferentialColumnInfo<User, ?>[] buildColumnInfos()
  {
    //noinspection unchecked
    return new ReferentialColumnInfo[]
      {
        new ReferentialColumnInfo<User, String>(RevuBundle.message("projectSettings.review.referential.user.table.login.title"))
        {
          public String valueOf(User user)
          {
            return user.getLogin();
          }
        },
        new ReferentialColumnInfo<User, String>(RevuBundle.message("projectSettings.review.referential.user.table.displayName.title"))
        {
          public String valueOf(User user)
          {
            return user.getDisplayName();
          }
        },
        new ReferentialColumnInfo<User, Boolean>(RevuBundle.message("userRoles.admin.text"))
        {
          public Boolean valueOf(User user)
          {
            return user.getRoles().contains(User.Role.ADMIN);
          }

          @Override
          public Class getColumnClass()
          {
            return Boolean.class;
          }

          @Override
          public int getWidth(JTable table)
          {
            return 55;
          }
        },
        new ReferentialColumnInfo<User, Boolean>(RevuBundle.message("userRoles.reviewer.text"))
        {
          public Boolean valueOf(User user)
          {
            return user.getRoles().contains(User.Role.REVIEWER);
          }

          @Override
          public Class getColumnClass()
          {
            return Boolean.class;
          }

          @Override
          public int getWidth(JTable table)
          {
            return 55;
          }
        },
        new ReferentialColumnInfo<User, Boolean>(RevuBundle.message("userRoles.author.text"))
        {
          public Boolean valueOf(User user)
          {
            return user.getRoles().contains(User.Role.AUTHOR);
          }

          @Override
          public Class getColumnClass()
          {
            return Boolean.class;
          }

          @Override
          public int getWidth(JTable table)
          {
            return 55;
          }
        }
      };
  }
}